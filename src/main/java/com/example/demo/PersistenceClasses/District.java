package com.example.demo.PersistenceClasses;
import java.util.ArrayList;
import com.example.demo.EnumClasses.*;
public class District {
    public int districtId;
    public StateName state;
    public ArrayList<Precinct> precincts;
    public ArrayList<float[]> boundary;
    
    public District(int districtId, StateName state,ArrayList<Precinct> precincts,ArrayList<float[]> boundary){
        this.districtId = districtId;
        this.state = state;
        this.precincts = precincts;
        this.boundary = boundary;
    }
    
    public int getDistrictId(){
        return districtId;
    }
    public void setDistrictId(int districtId){
        this.districtId = districtId;
    }
    
    public StateName getState(){
        return state;
    }
    public void setState(StateName state){
        this.state = state;
    }

    public ArrayList<Precinct> getPrecincts(){
        return precincts;
    }
    public void setPrecincts(ArrayList<Precinct> precincts){
        this.precincts = precincts;
    }

    public ArrayList<float[]> getBoundary(){
        return boundary;
    }
    public void setBoundary(ArrayList<float[]> boundary){
        this.boundary = boundary;
    }

    public float getVAP(EthnicGroup eg){
        return (float)2.5;
    }
    public int getTotalPopulation(){
        return 10;
    }
    public int getTotalCounty(){
        return 3;
    }
}
