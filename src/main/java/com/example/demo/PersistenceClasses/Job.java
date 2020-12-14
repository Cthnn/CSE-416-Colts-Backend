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
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.EnumClasses.*;
import com.example.demo.WrapperClasses.BoxPlotStatistic;
@Entity
public class Job {
    private Integer jobId;
    private int slurmId;
    private JobStatus status;
    private State state;

    private int plans;
    private double populationDeviation;
    private double compactness;
    private EthnicGroup ethnicGroup;

    private List<Districting> districtings;

    private double[][] vapMatrix;
    private double[][] vapMatrixTranspose;
    private double[][] boxPlotValues;
    private Districting averageDistricting;
    private Districting extremeDistricting;

    private int averageDistrictingIndex;
    private int extremeDistrictingIndex;
    
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
    public Integer getJobId(){
        return jobId;
    }
    public void setJobId(Integer jobId){
        this.jobId = jobId;
    }

    public int getSlurmId(){
        return slurmId;
    }

    public void setSlurmId(int id){
        slurmId = id;
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
    @OneToOne
    @JoinColumn(name = "extreme_districting_id", referencedColumnName="districting_id")
    public Districting getExtremeDistricting(){
        return extremeDistricting;
    }

    public void setExtremeDistricting(Districting dist) {
        extremeDistricting = dist;
    }
    @Transient
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "average_districting_id", referencedColumnName="districting_id")
    public Districting getAverageDistricting(){
        return averageDistricting;
    }

    public void setAverageDistricting(Districting dist){
        averageDistricting = dist;
    }

    @Transient
    @JsonIgnore
    public int getAverageDistrictingIndex(){
        return averageDistrictingIndex;
    }

    public void setAverageDistrictingIndex(int index) {
        averageDistrictingIndex = index;
    }

    @Transient
    @JsonIgnore
    public int getExtremeDistrictingIndex(){
        return extremeDistrictingIndex;
    }

    public void setExtremeDistrictingIndex(int index) {
        extremeDistrictingIndex = index;
    }

    public void initDistrictings(List<Districting> d){
        setDistrictings(d);
        getBoxPlotValues();
        generateDisplayDistrictings();
    }

    public void generateDisplayDistrictings(){
        double[] averageDistricting = getMatrixColumn(boxPlotValues, 2);
        double[] vapDifferences = new double[vapMatrix.length];
        for(int i = 0; i < vapMatrix.length; i++){
            vapDifferences[i] = districtingVapDifference(vapMatrix[i], averageDistricting);
        }

        int minIndex = 0;
        double min = vapDifferences[0];
        int extremeIndex = 0;
        double extreme = vapDifferences[0];

        for(int i = 0; i < vapDifferences.length; i++){
            if(vapDifferences[i] < min){
                min = vapDifferences[i];
                minIndex = i;
            }
            if(vapDifferences[i] > extreme){
                extreme = vapDifferences[i];
                extremeIndex = i;
            }
        }
        
        Districting minDistricting = districtings.get(minIndex);
        Districting extremeDistricting = districtings.get(extremeIndex);
        minDistricting.setType(DistrictingType.AVERAGE);
        extremeDistricting.setType(DistrictingType.EXTREME);
        setAverageDistrictingIndex(minIndex);
        setExtremeDistrictingIndex(extremeIndex);
        setAverageDistricting(minDistricting);
        setExtremeDistricting(extremeDistricting);
    }

    private double districtingVapDifference(double[] vap1, double[] vap2){
        double sum = 0;
        for(int i = 0; i < vap1.length; i++){
            sum += (vap1[i] - vap2[i]);
        }

        return sum/vap1.length;
    }

    public JSONObject generateSummaryFile(){
        return null;
    }

    @JsonIgnore
    @Transient
    private double[][] getVapMatrix(EthnicGroup eg){
        if(vapMatrix == null){
            double[][] matrix = new double[districtings.size()][state.getNumDistricts()];
            for(int i=0; i < matrix.length;i++){
                matrix[i] = districtings.get(i).getDistrictVAPPercentages(eg);
            }
            vapMatrix = matrix;
        }

        return vapMatrix;
    }

    @JsonIgnore
    @Transient
    public double[][] getVapMatrixTranspose() {
        return vapMatrixTranspose;
    }

    @JsonIgnore
    @Transient
    private double[] getMatrixColumn(double[][] matrix, int col){
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
            double[] vaps = getMatrixColumn(vapMatrix, i);
            output[i] = new BoxPlotStatistic(vaps).toArray();
        }

        return output;
    }

    private double[][] generateBoxPlotData(EthnicGroup eg){
        int numDistricts = state.getNumDistricts();
        System.out.println("NUMDISTRICT " + numDistricts);
        double[][] vapMatrix = getVapMatrix(eg);
        double[][] output = new double[numDistricts][5];

        for(int i=0; i < output.length;i++){
            double[] vaps = getMatrixColumn(vapMatrix, i);
            output[i] = vaps;
        }

        return output;
    }

    @Transient
    @JsonIgnore
    public double[][] getBoxPlotValues(){
        if(boxPlotValues == null)
            boxPlotValues = generateBoxPlot(ethnicGroup);
        return boxPlotValues;
    }

    @Transient
    @JsonIgnore
    public double[][] getBoxPlotData(){
        if(vapMatrixTranspose == null)
            vapMatrixTranspose = generateBoxPlotData(ethnicGroup);
        return vapMatrixTranspose;
    }

}
