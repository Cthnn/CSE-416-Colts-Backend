package com.example.demo.PersistenceClasses;
import java.util.ArrayList;
import java.util.HashSet;

import com.example.demo.EnumClasses.*;

public class District {

    private int districtId;
    private StateName state;
    private ArrayList<Precinct> precincts;
    private ArrayList<float[]> boundary;
    
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
        //might not need this
        return boundary;
    }
    public void setBoundary(ArrayList<float[]> boundary){
        //might not need this
        this.boundary = boundary;
    }

    public float getVAP(EthnicGroup eg){
        int totalPop = 0;
        int egPop = 0;
        for(int i = 0; i < precincts.size();i++){
            Precinct p = precincts.get(i);
            totalPop += p.getTotalPop();
            egPop += p.getEthnicPop().get(eg);
        }
        return ((float)egPop)/totalPop;
    }
    public int getTotalPopulation(){
        int count = 0;
        for(int i = 0; i < precincts.size();i++){
            count += precincts.get(i).getTotalPop();
        }
        return count;
    }
    public int getTotalCounty(){
        HashSet<Integer> ids = new HashSet<Integer>();
        for(int i = 0; i < precincts.size();i++){
            ids.add(precincts.get(i).getCountyId());
        }
        return ids.size();
    }
}
