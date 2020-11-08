package com.example.demo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.EnumClasses.*;
import com.example.demo.Repositories.*;
@Service
public class JobHandler {
    public ArrayList<Job> jobs = new ArrayList<Job>();

    @Autowired
    private JobRepository repository;

    public void init(){
        Job j = new Job(StateName.ALABAMA, 1000, 0.5, 50, EthnicGroup.ASIAN);
        j.setStatus(JobStatus.COMPLETED);
        repository.save(j);
    }
    
    public ArrayList<Job> getHistory() {
        ArrayList<Job> jobs = (ArrayList<Job>) repository.findAll();
        return jobs;
    }

    public int createJob(JSONObject params) {
        if (repository == null) {
            System.out.println("REPO is NULL");
            return 0;
        }
        Job j = repository.save(new Job((StateName) params.get("state"), ((Number) params.get("plans")).intValue(), ((Number) params.get("pop")).doubleValue(), ((Number) params.get("comp")).doubleValue(), EthnicGroup.valueOf((String) params.get("group"))));
        return j.getJobId();
    }

    public void cancelJob(int jobId) {
        Optional<Job> job = repository.findById(jobId);
        if(job.isPresent()){
            Job j = job.get();
            JobStatus status = j.getStatus();
            repository.delete(j);
            if(status == JobStatus.COMPLETED){
                deleteJob(jobId);
            }
        }else{
            System.out.println("ERROR: did not cancel job. Job does not exist with id: " + jobId);
        }
    }

    public void deleteJob(int jobId) {
        // delete the job files on seawulf
    }

    public ArrayList<Job> getStatuses(Integer[] jobIds) {
        // get statuses for specified jobs
        Iterable<Job> jobs = repository.findAllById((Iterable<Integer>) Arrays.asList(jobIds));
        return (ArrayList<Job>) jobs;
    }

    public void genSummary(int jobId) {
        // gen summary
    }

    public JSONObject getJobGeo(int jobId, DistrictingType type) {
        return new JSONObject();
    }

    public float[][] genBoxPlot(int jobId) {
        float[][] res = null;
        return res;
    }
}
