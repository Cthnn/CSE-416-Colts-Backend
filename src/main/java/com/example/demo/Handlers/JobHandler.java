package com.example.demo.Handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.SeawulfHelper;
import com.example.demo.ServerDispatcher;
import com.example.demo.EnumClasses.*;
import com.example.demo.Repositories.*;
import com.example.demo.WrapperClasses.JobParams;
import com.example.demo.WrapperClasses.PathBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Service
public class JobHandler {
    private Map<Integer, Job> jobs = new HashMap<>();
    @Autowired
    private JobRepository repository;
    @Autowired
    public StateHandler sh;

    @PostConstruct
    private void initJobs() {
        List<Job> repoJobs = (ArrayList<Job>) repository.findAll();
        for (int i = 0; i < repoJobs.size(); i++) {
            Job j = repoJobs.get(i);
            jobs.put(j.getJobId(), j);
        }

    }

    public Job getJob(int jobId) {
        Job j = jobs.get(jobId);
        if (j == null) {
            Optional<Job> job = repository.findById(jobId);
            if (job.isPresent())
                return job.get();
        }
        return null;
    }

    public List<Job> getHistory() {
        if(jobs.size() > 0)
            return new ArrayList<Job>(jobs.values());
        List<Job> jobs = (ArrayList<Job>) repository.findAll();
        return jobs;
    }

    public int createJob(JobParams params) {
        if (repository == null) {
            System.out.println("REPO is NULL");
            return 0;
        }

        Job j = repository.save(new Job(sh.getState(params.state), params.plans, params.pop, params.comp, params.group));
        jobs.put(j.getJobId(), j);
        ServerDispatcher.initiateJob(j);
        return j.getJobId();
    }

    public void updateStatus(int jobId, JobStatus status) {
        Job job = getJob(jobId);
        if (job != null) {
            job.setStatus(status);
            repository.save(job);
        }
    }

    public void cancelJob(int jobId) {
        Job job = getJob(jobId);
        if (job != null) {
            repository.delete(job);
            jobs.remove(job.getJobId());
        } else {
            System.out.println("ERROR: did not cancel job. Job does not exist with id: " + jobId);
        }
    }

    public List<Job> getStatuses(Integer[] jobIds) {
        List<Job> jobs = (ArrayList<Job>) repository.findAllById((Iterable<Integer>) Arrays.asList(jobIds));
        return jobs;
    }

    public Resource getSummary(int jobId) {
        // create the job summary json
        return null;
    }

    public double[][] getBoxPlot(int jobId) {
        Job job = getJob(jobId);
        if (job != null)
            return job.getBoxPlotValues();
        return null;
    }

    // TODO: change to return actual file
    public Resource getJobGeo(int jobId, DistrictingType type) {
        Job job = getJob(jobId);
        if (job != null) {
            return new FileSystemResource(PathBuilder.getDistrictPath(job.getState().getStateName()));
        } else {
            System.out.println("ERROR: Job does not exist with id: " + jobId);
            return null;
        }
    }

    public String generateGeoJson(Job job, DistrictingType type, Districting dist) {
        String fileOutput = PathBuilder.getJobDistrictPath(job.getJobId(), type);
        Path districtingFile = null;
        try {
            districtingFile = Files.createTempFile("districtings", ".json");
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(districtingFile.toFile(), dist);
            StateName state = job.getState().getStateName();
            ProcessBuilder builder = new ProcessBuilder("py", PathBuilder.getPythonScript(), "" + job.getJobId(),
                state.toString(), PathBuilder.getPrecinctPath(state), districtingFile.toString(), fileOutput);
            builder.redirectErrorStream(true);
            
            builder.inheritIO().start();
            return fileOutput;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<Precinct> createPrecincts(JSONArray precinctJSON, State state){
        List<Precinct> precincts = new ArrayList<Precinct>();
        Iterator<JSONObject> iterator = precinctJSON.iterator();
        
        for(Object id : precinctJSON.toArray()){
            Precinct precinct = state.getPrecinct((String)id);
            precincts.add(precinct);
        }
        return precincts;
    }

    private Set<District> createDistricts(JSONArray districtJSON, State state){
        Set<District> districts = new HashSet<District>();
        Iterator<JSONObject> iterator = districtJSON.iterator();
        while (iterator.hasNext()) {
            District district = new District();
            JSONObject obj = iterator.next();
            JSONArray precinctJSON = (JSONArray) obj.get("precincts");
            district.setPrecincts(createPrecincts(precinctJSON, state));
            district.setDistrictId(Math.toIntExact((Long)obj.get("id")));
            districts.add(district);
        }
        return districts;
    }

    public List<Districting> createDistrictings(JSONArray jobJSON, State state) {
        List<Districting> districtings = new ArrayList<Districting>();
        Iterator<JSONObject> iterator = jobJSON.iterator();
        while (iterator.hasNext()) {
            Districting districting = new Districting();
            JSONArray districtingJSON = (JSONArray) iterator.next().get("districts");
            districting.setDistricts(createDistricts(districtingJSON, state));
            districtings.add(districting);
        }
        return districtings;
    }


    //TODO: check every few minutes
    private void checkCompleteJobs(){
        for(int jobId : jobs.keySet()){
            Job j = getJob(jobId);
            if(j.getStatus() == JobStatus.QUEUED){
                JobStatus status = SeawulfHelper.getStatus(jobId);
                if(status != JobStatus.QUEUED){
                    updateStatus(jobId, status);
                }
            }
            else if(j.getStatus() == JobStatus.INPROGRESS){
                JobStatus status = SeawulfHelper.getStatus(jobId);
                if(status != JobStatus.INPROGRESS){
                    updateStatus(jobId, status);
                }

                if(status == JobStatus.COMPLETED){
                    JSONArray jobJSON = SeawulfHelper.getDistrictings(jobId);
                    List<Districting> districtings = createDistrictings(jobJSON, j.getState());
                    j.setDistrictings(districtings);
                }
            }
        }
    }


}
