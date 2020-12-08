package com.example.demo.PersistenceClasses;

import org.json.simple.JSONObject;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.EnumClasses.*;
import com.example.demo.WrapperClasses.BoxPlotStatistic;
@Entity
public class Job {
    private int jobId;

    private JobStatus status;
    private State state;

    private int plans;
    private double populationDeviation;
    private double compactness;
    private EthnicGroup ethnicGroup;

    private List<Districting> districtings;
    
    public Job() {}

    public Job(State state, int plans, double populationDeviation, double compactness, EthnicGroup eg){
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

    @ManyToOne
    @JoinColumn(name = "state_id")
    public State getState(){
        return state;
    }
    public void setState(State s){
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
    public List<Districting> getDistrictings(){
        return districtings;
    }
    public void setDistrictings(List<Districting> d){
        this.districtings = d;
    }

    @Transient
    @JsonIgnore
    public Districting getDistricting(DistrictingType type){
        if(type == DistrictingType.AVERAGE)
            return getAverageDistricting();
        else if(type == DistrictingType.EXTREME)
            return getExtremeDistricting();
        else if(type == DistrictingType.RANDOM)
            return getRandomDistricting();

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
    private Districting getRandomDistricting(){
        return null;
    }

    public JSONObject generateSummaryFile(){
        return null;
    }

    @JsonIgnore
    @Transient
    private double[][] getVapMatrix(EthnicGroup eg){
        double[][] vapMatrix = new double[districtings.size()][state.getNumDistricts()];
        for(int i=0; i < vapMatrix.length;i++){
            vapMatrix[i] = districtings.get(i).getDistrictVAPPercentages(eg);
        }

        return vapMatrix;
    }

    @JsonIgnore
    @Transient
    private double[] getVapMatrixColumn(double[][] matrix, int col){
        double[] vaps = new double[matrix.length];

        for(int i=0; i < matrix.length; i++){
            vaps[i] = matrix[i][col];
        }

        return vaps;
    }

    private double[][] generateBoxPlot(EthnicGroup eg){
        int numDistricts = state.getNumDistricts();
        System.out.println("NUMDISTRICT " + numDistricts);
        double[][] vapMatrix = getVapMatrix(eg);
        double[][] output = new double[numDistricts][5];

        for(int i=0; i < output.length;i++){
            double[] vaps = getVapMatrixColumn(vapMatrix, i);
            output[i] = new BoxPlotStatistic(vaps).toArray();
        }

        return output;
    }

    @Transient
    @JsonIgnore
    public double[][] getBoxPlotValues(){
        return generateBoxPlot(ethnicGroup);
    }

}
