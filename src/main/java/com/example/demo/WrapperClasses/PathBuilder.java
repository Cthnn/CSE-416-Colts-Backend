package com.example.demo.WrapperClasses;
import com.example.demo.EnumClasses.*;

public class PathBuilder {
    public static String getDistrictPath(StateName state){
        return "src/main/resources/districts/"+state.toString().toLowerCase()+"_districts.json";
    }
    public static String getPrecinctPath(StateName state){
        return "src/main/resources/precincts/"+state.toString().toLowerCase()+"_precincts.json";
    }
    public static String getHeatMapPath(StateName state){
        return "src/main/resources/heatmaps/"+state.toString().toLowerCase()+"_heatmap.json";
    }

    public static String getJobDistrictPath(int jobId, DistrictingType type){
        return "src/main/resources/job_districts/"+jobId+"_"+type.toString().toLowerCase()+".json";
    }
    public static String getJobPath(int jobId){
        return "src/main/resources/jobs/"+jobId+"_districtings.json";
    }
    public static String getJobSummary(int jobId){
        return "src/main/resources/summaries/"+jobId+"_summary.json";
    }
    public static String getPythonScript(){
        return "src/main/resources/scripts/union.py";
    }
    public static String getSummaryScript(){
        return "src/main/resources/scripts/summary.py";
    }
    public static String getAlgorithmScript(){
        return "src/main/resources/scripts/algorithm.py";
    }
    public static String getAlgorithmInput(StateName state){
        return "src/main/resources/algorithm_input/"+state.toString().toLowerCase()+"_input.json";
    }

}