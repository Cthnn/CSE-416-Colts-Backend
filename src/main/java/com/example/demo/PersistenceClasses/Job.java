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
    private int jobId;

    private JobStatus status;
    private StateName state;

    private int plans;
    private double populationDeviation;
    private double compactness;
    private EthnicGroup ethnicGroup;

    private Districting[] districtings;
    
    public Job() {}

    public Job(StateName state, int plans, double populationDeviation, double compactness, EthnicGroup eg){
        this.status = JobStatus.QUEUED;
        this.state = state;
        this.plans = plans;
        this.populationDeviation = populationDeviation;
        this.compactness = compactness;
        this.ethnicGroup = eg;
    }

    @Id 
    @GeneratedValue
    public int getJobId(){
        return jobId;
    }
    public void setJobId(int jobId){
        this.jobId = jobId;
    }

    @Enumerated(EnumType.STRING)
    public JobStatus getStatus(){
        return status;
    }
    public void setStatus(JobStatus status){
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    public StateName getState(){
        return state;
    }
    public void setState(StateName s){
        state = s;
    }

    public int getPlans(){
        return plans;
    }
    public void setPlans(int p){
        plans = p;
    }

    public double getPopulationDeviation(){
        return populationDeviation;
    }
    public void setPopulationDeviation(double pd){
        populationDeviation = pd;
    }

    public double getCompactness(){
        return compactness;
    }
    public void setCompactness(double comp) {
        compactness = comp;
    }

    @Enumerated(EnumType.STRING)
    public EthnicGroup getEthnicGroup(){
        return ethnicGroup;
    }
    public void setEthnicGroup(EthnicGroup eg){
        this.ethnicGroup = eg;
    }

    @Transient
    @JsonIgnore
    public Districting[] getDistrictings(){
        return districtings;
    }
    public void setDistrictings(Districting[] d){
        this.districtings = d;
    }

    public float[][] generateBoxPlot(){
        float [][] res = null;
        return res;
    }

    @Transient
    @JsonIgnore
    public Districting getDistricting(DistrictingType type){
        return null;
    }
    @Transient
    @JsonIgnore
    private Districting getExtremeDistricting(){
        return null;
    }
    @Transient
    @JsonIgnore
    private Districting getAverageDistricting(){
        return null;
    }

    @Transient
    @JsonIgnore
    private JSONObject generateGeoJson(Districting dist){
        return null;
    }

    @Transient
    @JsonIgnore
    public JSONObject getPrecinctGeoJson(){
        return null;
    }
    public JSONObject generateSummaryFile(){
        return null;
    }

    @Transient
    @JsonIgnore
    public float[][] getBoxPlotValues(EthnicGroup eg){
        //Thought about this a little bit more and maybe this belongs in the Job class? Box plot values are gathered from across multiple districtings.
        float[][] res = null;
        return res;
    }

}
