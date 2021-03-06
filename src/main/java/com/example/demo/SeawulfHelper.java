package com.example.demo;

import org.apache.catalina.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.demo.PersistenceClasses.*;
import com.example.demo.WrapperClasses.PathBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.example.demo.EnumClasses.*;

public class SeawulfHelper {
    public static JSONObject queue(Job j) {
        //Probably not necessary for now
        return new JSONObject();
    }

    public static JSONObject sendFile(JSONObject data) {
        //Probably not necessary for now
        return new JSONObject();
    }

    public static JSONArray getDistrictings(Job j) {
        JSONParser parser = new JSONParser();
        try {
            File f = new File(PathBuilder.getJobPath(j.getJobId()));
            if(j.getSlurmId() > 0 && j.getStatus() == JobStatus.COMPLETED && !f.exists()){
                ServerDispatcher.retrieveResults(j.getJobId());
            }
            JSONArray obj = (JSONArray) parser.parse(new FileReader(PathBuilder.getJobPath(j.getJobId())));
            System.out.println("READ JSON FROM FILE");
            return obj;
        } catch (IOException | ParseException e) {
        }
        return null;
    }
    public static JobStatus getStatus(int slurmId,int jobId, JobStatus jobStatus){
        try{
            JobStatus status = ServerDispatcher.seawulfStatus(slurmId, jobId,jobStatus);
            return status;
        }catch (IOException e){
            System.out.println(e);
            return null;
        }
    }
    public static JSONObject getGeoJSON(int jobId, DistrictingType type){
        //Probably not necessary for now
        return new JSONObject();
    }
    public static void abortInProgress(int jobId){
        //Same as cancel
    }
    public static void removeFiles(int jobId){
        try{
            ServerDispatcher.removeFiles(jobId);
        }catch(Exception e){
            System.out.println("could not remove files");
        }        
    }
}
