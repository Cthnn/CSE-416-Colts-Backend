package com.example.demo;

import org.json.simple.JSONObject;
public class Job {
    public int jobId;
    public BatchStatus status;
    public Districting[] job;
    public EthnicGroup eg;
    public Job(int jobId, BatchStatus status, Districting[] job, EthnicGroup eg){
        this.jobId = jobId;
        this.status = status;
        this.job = job;
        this.eg = eg;
    }
    public int getJobId(){
        return jobId;
    }
    public BatchStatus getStatus(){
        return status;
    }
    public Districting[] getJob(){
        return job;
    }
    public EthnicGroup getEthnicGroup(){
        return eg;
    }
    public void setJobId(int jobId){
        this.jobId = jobId;
    }
    public void setStatus(BatchStatus status){
        this.status = status;
    }
    public void setJob(Districting[] job){
        this.job = job;
    }
    public void setEthnicGroup(EthnicGroup eg){
        this.eg = eg;
    }
    public float[][] generateBoxPlot(){
        float [][] res = null;
        return res;
    }
    public Districting getDistricting(DistrictingType type){
        return null;
    }
    private Districting getExtremeDistricting(){
        return null;
    }
    private Districting getAverageDistricting(){
        return null;
    }
    private JSONObject generateGeoJson(Districting dist){
        return null;
    }
    public JSONObject getPrecinctGeoJson(){
        return null;
    }
    public JSONObject generateSummaryFile(){
        return null;
    }

}
