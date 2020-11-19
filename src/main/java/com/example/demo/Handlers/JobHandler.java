package com.example.demo.Handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.EnumClasses.*;
import com.example.demo.Repositories.*;
import com.example.demo.WrapperClasses.JobParams;
@Service
public class JobHandler {
    @Autowired
    private JobRepository repository;

    
    public List<Job> getHistory() {
        List<Job> jobs = (ArrayList<Job>) repository.findAll();
        return jobs;
    }

    public int createJob(JobParams params) {
        if (repository == null) {
            System.out.println("REPO is NULL");
            return 0;
        }
        Job j = repository.save(new Job(params.state, params.plans, params.pop, params.comp, params.group));
        return j.getJobId();
    }

    public void cancelJob(int jobId) {
        Optional<Job> job = repository.findById(jobId);
        if(job.isPresent()){
            Job j = job.get();
            JobStatus status = j.getStatus();
            //repository.delete(j); -- Don't delete cancelled jobs.
            if(status == JobStatus.COMPLETED || status == JobStatus.ABORTED || status == JobStatus.CANCELLED){
                repository.deleteById(jobId);
                //Use information from Job object obtained from JobRepo, and delete associated files in seawulf.
            }else if(status == JobStatus.QUEUED || status == JobStatus.INPROGRESS){
                j.setStatus(JobStatus.CANCELLED);
                //Cancel on seawulf
                repository.save(j);
            }
        }else{
            System.out.println("ERROR: did not cancel job. Job does not exist with id: " + jobId);
        }
    }
    public List<Job> getStatuses(Integer[] jobIds) {
        // get statuses for specified jobs
        List<Job> jobs = (ArrayList<Job>) repository.findAllById((Iterable<Integer>) Arrays.asList(jobIds));
        return jobs;
    }

    public double[][] genSummary(int jobId) {
        // gen summary
        return genBoxPlot(jobId);
    }

    public JSONObject getJobGeo(int jobId, DistrictingType type) {
        return new JSONObject();
    }

    public double[][] genBoxPlot(int jobId) {
        double [][] res = new double[10][5];
        for(int i =0; i < res.length;i++){
            res[i][0] = 0+((0.025)*i);
            res[i][1] = 0.0375+((0.025)*i);
            res[i][2] = 0.075+((0.025)*i);
            res[i][3] = 0.1125+((0.025)*i);
            res[i][4] = 0.15+((0.025)*i);
        }
        return res;
    }
}
