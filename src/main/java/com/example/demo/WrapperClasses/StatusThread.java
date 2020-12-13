package com.example.demo.WrapperClasses;

import java.util.ConcurrentModificationException;

import com.example.demo.Handlers.JobHandler;

public class StatusThread implements Runnable {
    private JobHandler jh;

    public StatusThread(JobHandler jh) {
        this.jh = jh;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Checking Status");
            try {
                jh.checkCompleteJobs();
                System.out.println("Finished Checking Status. Sleeping");
                Thread.sleep((5 * 60000));
            } catch (InterruptedException e) {
                System.out.println("Check Status Interrupted");
            } catch (ConcurrentModificationException e) {
                System.out.println(e);
            }
        }
    }
    
}
