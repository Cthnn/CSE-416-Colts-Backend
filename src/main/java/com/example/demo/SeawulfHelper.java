package com.example.demo;
import org.json.simple.JSONObject;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.EnumClasses.*;
public class SeawulfHelper {
    public static JSONObject queue(JSONObject params, int jobId){
        return new JSONObject();
    }
    public static JSONObject sendFile(JSONObject data){
        return new JSONObject();
    }
    public static JSONObject getOriginal(State state,BoundaryType type){
        return new JSONObject();
    }
    public static JSONObject getData(int jobId){
        return new JSONObject();
    }
    public static JobStatus getStatus(int jobId){
        return JobStatus.COMPLETED;
    }
    public static JSONObject getGeoJSON(int jobId, DistrictingType type){
        return new JSONObject();
    }
    public static JSONObject abortInProgress(int jobId){
        return new JSONObject();
    }
    public static JSONObject removeFiles(int jobId){
        return new JSONObject();
    }
}
