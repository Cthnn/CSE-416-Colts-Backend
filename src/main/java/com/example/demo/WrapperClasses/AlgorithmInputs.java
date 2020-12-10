package com.example.demo.WrapperClasses;
import com.example.demo.PersistenceClasses.Job;

public class AlgorithmInputs {
    public String targetDistricts;
    public String plans;
    public String maxIterations = "15";
    public String compactness;
    public String populationDeviation;
    public String inputFile;
    public String outputFile;

    public AlgorithmInputs(Job j, boolean local){
        targetDistricts = "" + j.getState().getNumDistricts();
        plans = "" + j.getPlans();
        compactness = "" + j.getCompactness();
        populationDeviation = "" + j.getPopulationDeviation();

        if(local){
            inputFile = PathBuilder.getAlgorithmInput(j.getState().getStateName());
            outputFile  = PathBuilder.getJobPath(j.getJobId());          
        }
        else{

        }
    }
}