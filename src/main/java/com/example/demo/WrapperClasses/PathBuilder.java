package com.example.demo.WrapperClasses;
import com.example.demo.EnumClasses.*;

public class PathBuilder {
    public static String getDistrictPath(StateName state){
        return "src/main/resources/districts/"+state.toString()+"_districts.json";
    }
    public static String getPrecinctPath(StateName state){
        return "src/main/resources/precincts/"+state.toString()+"_precincts.json";
    }
    public static String getHeatMapPath(StateName state){
        return "src/main/resources/heatmaps/"+state.toString()+"_heatmap.json";
    }

    public static String getJobDistrictPath(int jobId, DistrictingType type){
        return "src/main/resources/job_districts/"+jobId+"_"+type.toString().toLowerCase()+".json";
    }
    public static String getJobPath(int jobId){
        return "src/main/resources/jobs/"+jobId+"_districtings.json";
    }
    public static String getPythonScript(){
        return "src/main/resources/scripts/union.py";
    }

}