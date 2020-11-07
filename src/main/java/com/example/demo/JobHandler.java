package com.example.demo;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobHandler {
    public ArrayList<Job> jobs = new ArrayList<Job>();

    @Autowired
    private JobRepository repository;

    public ArrayList<Job> getHistory() {
        return (ArrayList<Job>) repository.findAll();
    }

    public int createJob(JSONObject params) {
        if (repository == null) {
            System.out.println("REPO is NULL");
            return 0;
        }
        Job j = repository.save(new Job(10000, 0.5, 0.4, EthnicGroup.ASIAN));
        //System.out.println(repository.findAll());
        return j.getJobId();
    }

    public void cancelJob(int jobId) {
        // cancel the job
    }

    public void deleteJob(int jobId) {
        // delete the job
    }

    public JSONObject getStatuses(int[] jobIds) {
        // get statuses for specified jobs
        return new JSONObject();
    }

    public void genSummary(int jobId, Precinct[] precincts) {
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
