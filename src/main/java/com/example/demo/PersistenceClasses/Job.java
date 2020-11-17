package com.example.demo.PersistenceClasses;

import org.json.simple.JSONObject;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private JobStatus status;
    @Enumerated(EnumType.STRING)
    private StateName state;
    private int plans;
    private double populationDeviation;
    private double compactness;
    @Enumerated(EnumType.STRING)
    private EthnicGroup ethnicGroup;

    @Transient
    @JsonIgnore
    private Districting[] job;
    
    public Job() {
        
    }

    public Job(StateName state, int plans, double populationDeviation, double compactness, EthnicGroup eg){
        this.status = JobStatus.QUEUED;
        this.state = state;
        this.plans = plans;
        this.populationDeviation = populationDeviation;
        this.compactness = compactness;
        this.ethnicGroup = eg;
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
        return plans;
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
        return ethnicGroup;
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
        this.ethnicGroup = eg;
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
    public float[][] getBoxPlotValues(EthnicGroup eg){
        //Thought about this a little bit more and maybe this belongs in the Job class? Box plot values are gathered from across multiple districtings.
        float[][] res = null;
        return res;
    }

}
