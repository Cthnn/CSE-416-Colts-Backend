package com.example.demo.PersistenceClasses;

import java.util.HashSet;
import java.util.List;

import com.example.demo.EnumClasses.*;

public class District {

    private int districtId;
    private StateName state;
    private List<Precinct> precincts;

    
    public District(int districtId, StateName state,List<Precinct> precincts){
        this.districtId = districtId;
        this.state = state;
        this.precincts = precincts;
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

    public List<Precinct> getPrecincts(){
        return precincts;
    }
    public void setPrecincts(List<Precinct> precincts){
        this.precincts = precincts;
    }

    public float getVAP(EthnicGroup eg){
        int totalPop = 0;
        int egPop = 0;
        for(int i = 0; i < precincts.size();i++){
            Precinct p = precincts.get(i);
            totalPop += p.getDemographic().getTotalPopulation();
            egPop += p.getDemographic().getVapPopulations().get(eg);
        }
        return ((float)egPop)/totalPop;
    }
    public int getTotalPopulation(){
        int count = 0;
        for(int i = 0; i < precincts.size();i++){
            count += precincts.get(i).getDemographic().getTotalPopulation();
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
