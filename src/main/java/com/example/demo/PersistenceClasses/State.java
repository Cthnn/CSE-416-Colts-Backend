package com.example.demo.PersistenceClasses;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import com.example.demo.EnumClasses.*;
public class State {
    public String name;
    public ArrayList<District> districts;
    public ArrayList<Precinct> precincts;

    public State(String name, ArrayList<District> districts,ArrayList<Precinct> precincts){
        this.name = name;
        this.districts = districts;
        this.precincts = precincts;
    }
    
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public ArrayList<District> getDistricts(){
        return districts;
    }
    public void setDistricts(ArrayList<District> districts){
        this.districts = districts;
    }

    public ArrayList<Precinct> getPrecincts(){
        return precincts;
    }
    public void setPrecincts(ArrayList<Precinct> precincts){
        this.precincts = precincts;
    }
   
    public JSONObject getHeatMap(EthnicGroup eg){
        return new JSONObject();
    }
    public JSONObject toJSON(){
        return new JSONObject();
    }
}
