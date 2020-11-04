package com.example.demo;
import java.util.ArrayList;

import org.json.simple.JSONObject;
public class JobHandler {
    public ArrayList<Job> jobs = new  ArrayList<Job>();
    public JSONObject getHistory(){
        return new JSONObject();
    }
    public int createJob(JSONObject params){
        return 10;
    }
    public void cancelJob(int jobId){
        //cancel the job
    }
    public void deleteJob(int jobId){
        //delete the job
    }
    public JSONObject getStatuses(int[] jobIds){
        //get statuses for specified jobs
        return new JSONObject();
    }
    public void genSummary(int jobId, Precinct[] precincts){
        //gen summary
    }
    public JSONObject getJobGeo(int jobId, DistrictingType type){
        return new JSONObject();
    }
    public float[][] genBoxPlot(int jobId){
        float[][] res = null;
        return res;
    }
}
