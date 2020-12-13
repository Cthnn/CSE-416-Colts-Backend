package com.example.demo.WrapperClasses;

import com.example.demo.Repositories.JobRepository;

import java.io.IOException;

import com.example.demo.ServerDispatcher;
import com.example.demo.EnumClasses.JobStatus;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.SeawulfHelper;
public class DeleteThread implements Runnable {
    private Job j;
    private JobRepository jobrepo;

    public DeleteThread(Job j,JobRepository jobrepo) {
        this.j = j;
        this.jobrepo = jobrepo;
    }

    public void run() {
        jobrepo.delete(j);
        try{
            ServerDispatcher.cancelJob(j.getSlurmId());
            SeawulfHelper.removeFiles(j.getJobId());
        }catch(Exception e){
            System.out.println("ERROR: did not cancel job. Job does not exist with id: " + j.getJobId());
        }
    }
}
