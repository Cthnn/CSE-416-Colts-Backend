package com.example.demo.WrapperClasses;

import com.example.demo.Repositories.JobRepository;

import java.io.IOException;

import com.example.demo.ServerDispatcher;
import com.example.demo.EnumClasses.JobStatus;
import com.example.demo.PersistenceClasses.*;

public class JobThread implements Runnable {
    private Job j;
    private JobRepository jobRepo;

    public JobThread(Job j, JobRepository jobRepo) {
        this.j = j;
        this.jobRepo = jobRepo;
    }

    public void run() {
        try {
            ServerDispatcher.initiateJob(j, jobRepo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Job Could not be Queued");
            j.setStatus(JobStatus.ABORTED);
            jobRepo.save(j);
        }

    }
}
