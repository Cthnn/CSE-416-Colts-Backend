package com.example.demo.Handlers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.EnumClasses.*;
import com.example.demo.Repositories.*;
import com.example.demo.WrapperClasses.JobParams;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Service
public class JobHandler {
    @Autowired
    private JobRepository repository;

    public Job getJob(int jobId){
        Optional<Job> job = repository.findById(jobId);
        if(job.isPresent())
            return job.get();
        return null;
    }

    public List<Job> getHistory() {
        List<Job> jobs = (ArrayList<Job>) repository.findAll();
        return jobs;
    }

    public int createJob(int stateId, JobParams params) {
        if (repository == null) {
            System.out.println("REPO is NULL");
            return 0;
        }
        Job j = repository.save(new Job(stateId, params.plans, params.pop, params.comp, params.group));
        return j.getJobId();
    }

    public void updateStatus(int jobId, JobStatus status){
        Job job = getJob(jobId);
        if(job != null){
            job.setStatus(status);
            repository.save(job);
        }
    }

    public void cancelJob(int jobId) {
        Job job = getJob(jobId);
        if(job != null){
            repository.delete(job);
        }else{
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
        if(job != null)
            return job.getBoxPlotValues();
        return null;
    }

    //TODO: change to return actual file
    public Resource getJobGeo(int jobId, DistrictingType type) {
        Job job = getJob(jobId);
        if(job != null){
            String state = StateHandler.getState(job.getStateId()).toString().toLowerCase();
            Path path = Paths.get("src/main/resources/districts/"+state+"_districts.json");
            return new FileSystemResource(path);
        }else{
            System.out.println("ERROR: Job does not exist with id: " + jobId);
            return null;
        }
    }
}
