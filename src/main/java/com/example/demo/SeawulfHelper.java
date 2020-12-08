package com.example.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.demo.PersistenceClasses.*;
import com.example.demo.WrapperClasses.PathBuilder;

import java.io.FileReader;
import java.io.IOException;

import com.example.demo.EnumClasses.*;

public class SeawulfHelper {
    public static JSONObject queue(Job j) {
        return new JSONObject();
    }

    public static JSONObject sendFile(JSONObject data) {
        return new JSONObject();
    }

    public static JSONArray getDistrictings(int jobId) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray obj = (JSONArray) parser.parse(new FileReader(PathBuilder.getJobPath(jobId)));
            System.out.println("READ JSON FROM FILE");
            return obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new JSONArray();
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
