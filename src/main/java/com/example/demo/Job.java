package com.example.demo;

import org.json.simple.JSONObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Job {

    @Id 
    @GeneratedValue
    private int jobId;
    private BatchStatus status;
    private Districting[] job;
    private int runs;
    private double populationDeviation;
    private double compactness;
    private EthnicGroup eg;

    public Job() {
        
    }

    public Job(int runs, double populationDeviation, double compactness, EthnicGroup eg){
        this.status = BatchStatus.QUEUED;
        this.runs = runs;
        this.populationDeviation = populationDeviation;
        this.compactness = compactness;
        this.eg = eg;
    }

    public void printParams(){
        System.out.println(this.jobId);
        System.out.println(this.status.toString());
        System.out.println(this.runs);
        System.out.println(this.populationDeviation);
        System.out.println(this.compactness);
        System.out.println(this.eg.toString());
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
