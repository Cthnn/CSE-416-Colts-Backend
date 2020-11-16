package com.example.demo.Handlers;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.simple.JSONObject;
import org.springframework.core.io.FileSystemResource;

import com.example.demo.EnumClasses.*;
public class StateHandler{
    public JSONObject getDistricts(StateName s){
        return null;
    }
    public FileSystemResource getPrecincts(StateName s){
        String state = s.toString().toLowerCase();
        Path path = Paths.get("src/main/resources/"+state+".json");
        return new FileSystemResource(path); 
    }
    public JSONObject getDemographic(StateName s,int precinctId){
        return null;
    }
    public FileSystemResource getHeatMap(StateName s){
        String state = s.toString().toLowerCase();
        Path path = Paths.get("src/main/resources/"+state+"_heatmap.json");
        return new FileSystemResource(path); 
    }
}