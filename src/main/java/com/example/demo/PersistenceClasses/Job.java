package com.example.demo.PersistenceClasses;

import org.json.simple.JSONObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.EnumClasses.*;
@Entity
public class Job {

    @Id 
    @GeneratedValue
    private int jobId;
    private JobStatus status;
    private StateName state;
    private int runs;
    private double populationDeviation;
    private double compactness;
    private EthnicGroup eg;

    @Transient
    @JsonIgnore
    private Districting[] job;
    public Job() {
        
    }

    public Job(StateName state, int runs, double populationDeviation, double compactness, EthnicGroup eg){
        this.status = JobStatus.COMPLETED;
        this.state = state;
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
    public StateName getState(){
        return state;
    }
    public JobStatus getStatus(){
        return status;
    }
    public int getRuns(){
        return runs;
    }
    public double getPopulationDeviation(){
        return populationDeviation;
    }
    public double getCompactness(){
        return compactness;
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
    public void setStatus(JobStatus status){
        this.status = status;
    }
    public void setJob(Districting[] job){
        this.job = job;
    }
    public void setEthnicGroup(EthnicGroup eg){
        this.eg = eg;
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
    // Don't have use for this method
    // public JSONObject getPrecinctGeoJson(){
    //     return null;
    // }
    public JSONObject generateSummaryFile(){
        //Precinct and District Geometry
        //Box plot values??
        //Generate the summary file so that we dan just get poxplotvalues from it later
        return null;
    }
    private float[][] generateBoxPlot(EthnicGroup eg){
        float [][] res = null;
        return res;
    }
    public float[][] getBoxPlotValues(EthnicGroup eg){
        //Thought about this a little bit more and maybe this belongs in the Job class? Box plot values are gathered from across multiple districtings.
        float[][] res = null;
        return res;
    }

}
