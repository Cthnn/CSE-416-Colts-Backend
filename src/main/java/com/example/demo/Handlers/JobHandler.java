package com.example.demo.Handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.example.demo.WrapperClasses.DeleteThread;
import com.example.demo.WrapperClasses.JobParams;
import com.example.demo.WrapperClasses.JobThread;
import com.example.demo.WrapperClasses.PathBuilder;
import com.example.demo.WrapperClasses.StatusThread;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Service
public class JobHandler {
    private List<Job> jobs = new ArrayList<>();
    @Autowired
    private JobRepository jobRepo;
    @Autowired
    private DistrictingRepository districtingRepo;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    public StateHandler sh;

    @PostConstruct
    private void initJobs() {
        jobs = (ArrayList<Job>) jobRepo.findAll();
        for(Job j : jobs){
            initJobDistrictings(j);
        }
        StatusThread st = new StatusThread(this);
        Thread t = new Thread(st);
        t.start();
    }

    public Job getJob(int jobId) {
        for(Job j : jobs){
            if(j.getJobId() == jobId)
                return j;
        }
        Optional<Job> job = jobRepo.findById(jobId);
        if (job.isPresent())
            return job.get();
        
        return null;
    }

    public List<Job> getHistory() {
        if(jobs.size() > 0)
            return jobs;
        List<Job> jobs = (ArrayList<Job>) jobRepo.findAll();
        return jobs;
    }

    public int createJob(JobParams params) {
        if (jobRepo == null) {
            System.out.println("REPO is NULL");
            return 0;
        }
        
        Job j = new Job(sh.getState(params.state), params.plans, params.pop, params.comp, params.group);
        j = jobRepo.save(j);
        jobs.add(j);
        JobThread jt = new JobThread(j,this.jobRepo);
        Thread t = new Thread(jt);
        t.start();
        return j.getJobId();
    }

    public void updateStatus(int jobId, JobStatus status) {
        Job job = getJob(jobId);
        if (job != null) {
            job.setStatus(status);
            jobRepo.save(job);
        }
    }

    public void cancelJob(int jobId) {
        Job job = getJob(jobId);
        if (job != null) {
            jobs.remove(job);
            System.out.println("Removed from Jobs");
            DeleteThread dt = new DeleteThread(job,jobRepo);
            Thread t = new Thread(dt);
            t.start();
        } else {
            System.out.println("ERROR: did not cancel job. Job does not exist with id: " + jobId);
        }
    }

    public List<Job> getStatuses(Integer[] jobIds) {
        List<Job> jobs = (ArrayList<Job>) jobRepo.findAllById((Iterable<Integer>) Arrays.asList(jobIds));
        return jobs;
    }

    public double[][] getBoxPlot(int jobId) {
        Job job = getJob(jobId);
        if (job != null)
            return job.getBoxPlotValues();
        return null;
    }

    public Resource getSummary(int jobId) {
        Job job = getJob(jobId);
        if (job != null  && job.getStatus() == JobStatus.COMPLETED) {
            File f = new File(PathBuilder.getJobSummary(jobId));
            if(!f.exists()){
                generateSummary(job);
            }
            return new FileSystemResource(PathBuilder.getJobSummary(jobId));
        } else {
            System.out.println("ERROR: Job does not exist or is not completed: " + jobId);
            return null;
        }
    }

    // TODO: change to return actual file
    public Resource getJobGeo(int jobId, DistrictingType type) {
        Job job = getJob(jobId);
        if (job != null && job.getStatus() == JobStatus.COMPLETED) {
            File f = new File(PathBuilder.getJobDistrictPath(jobId, type));
            if(!f.exists()){
                System.out.println("creating geojson file");
                if(type == DistrictingType.AVERAGE)
                    generateGeoJson(job, type, job.getAverageDistricting());
                else generateGeoJson(job, type, job.getExtremeDistricting());
            }
            System.out.println("sending geojson file");
            return new FileSystemResource(PathBuilder.getJobDistrictPath(jobId, type));
        } else {
            System.out.println("ERROR: Job does not exist or is not completed: " + jobId);
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
            
            builder.inheritIO().start().waitFor();
            return fileOutput;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateSummary(Job job){
        String fileOutput = PathBuilder.getJobSummary(job.getJobId());
        try {
            String state = job.getState().getStateName().toString();
            String eg = job.getEthnicGroup().toString();
            ProcessBuilder builder = new ProcessBuilder("py", PathBuilder.getSummaryScript(), ""+job.getJobId(),
                state.toString(), ""+job.getCompactness(), ""+job.getPopulationDeviation(), eg, 
                ""+job.getAverageDistrictingIndex(), ""+job.getExtremeDistrictingIndex(), fileOutput);
    
            builder.redirectErrorStream(true);
            builder.inheritIO().start().waitFor();
            return fileOutput;
        } catch (IOException | InterruptedException e) {
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
            district.setIndex(Math.toIntExact((Long)obj.get("id")));
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

    public List<Districting> initJobDistrictings(Job j){
        JSONArray jobJSON = null;
        if(j.getStatus() == JobStatus.COMPLETED){
            jobJSON = SeawulfHelper.getDistrictings(j);
        }
        if(jobJSON == null)
            return new ArrayList<Districting>();
        List<Districting> districtings = createDistrictings(jobJSON, j.getState());
        j.initDistrictings(districtings);
        return districtings;
    }

    public void saveJob(Job j){
        saveDistricting(j.getAverageDistricting());
        saveDistricting(j.getExtremeDistricting());
        jobRepo.save(j);
    }

    public int saveDistricting(Districting districting){
        Set<District> districts = districting.getDistricts();
        districting.setDistricts(null);
        districting = districtingRepo.save(districting);
        System.out.println("Saved districting with id: " + districting.getDistrictingId());
        for(District d : districts){
            d.setDistrictingId(districting.getDistrictingId());
            d = districtRepo.save(d);
            System.out.println("Saved district with id: " + d.getDistrictId());
        }
        districting.setDistricts(districts);
        return districting.getDistrictingId();
    }

    public void checkCompleteJobs(){
        for(Job j : jobs){
            System.out.println("Job: " +j.getJobId() + " , Slurm: "+ j.getSlurmId());
            if(j.getStatus() == JobStatus.QUEUED){
                JobStatus status = SeawulfHelper.getStatus(j.getSlurmId(),j.getJobId());
                if (status == null){
                    continue;
                }
                if(status != JobStatus.QUEUED){
                    updateStatus(j.getJobId(), status);
                }
            }
            else if(j.getStatus() == JobStatus.INPROGRESS){
                JobStatus status = JobStatus.INPROGRESS;
                if(j.getSlurmId() == 0){
                    File f = new File(PathBuilder.getJobPath(j.getJobId()));
                    if(f.exists()){
                        status = JobStatus.COMPLETED;
                    }
                }else{
                    status = SeawulfHelper.getStatus(j.getSlurmId(),j.getJobId());
                    if (status == null){
                        continue;
                    }
                }
                if(status != JobStatus.INPROGRESS){
                    updateStatus(j.getJobId(), status);
                }

                if(status == JobStatus.COMPLETED){
                    initJobDistrictings(j);
                    generateGeoJson(j, DistrictingType.AVERAGE, j.getAverageDistricting());
                    generateGeoJson(j, DistrictingType.EXTREME, j.getExtremeDistricting());
                    //saveJob(j);
                }
            }
        }
    }


}
