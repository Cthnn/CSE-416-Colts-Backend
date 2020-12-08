package com.example.demo;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.example.demo.PersistenceClasses.Job;
public class ServerDispatcher {
    private static int thresh = 0;
    public static void initiateJob(Job j){
        //Edit the slurm script based on params
        if (j.getPlans() > thresh){
            try{
                //Start the Process
                ProcessBuilder exec = new ProcessBuilder("bash","src/main/resources/trigger.sh");
                Process process = exec.start();
                //Read the output created from running the script as an input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                    builder.append(line);
                }
                //Build String and extract the slurm id so we can check for status later
                String result = builder.toString();
                String[] arr = result.split(" ");
                int slurmId = Integer.parseInt(arr[arr.length-1]);
                System.out.println(slurmId);
            }catch(Exception e){
                System.out.println(e);
            }
        }else{
            //Run Locally
        }
    }
}
