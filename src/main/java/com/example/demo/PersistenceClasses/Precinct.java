package com.example.demo.PersistenceClasses;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.example.demo.EnumClasses.*;

@Entity
public class Precinct {
    @Id
    public String gisId;
    public String precinctId;
    public String precinctName;
    public String countyname;
    public int countyId;
    public StateName state;
    public int totalPop;
    public int white;
    public int black;
    public int asian;
    public int hispanic;
    public int american_indian;
    public int native_hawaiian;
    public int other;

    public ArrayList<Neighbors> neighbors;
    
    public Precinct(String precinctId, StateName state, ArrayList<Neighbors> neighbors,int countyId,int totalPop,HashMap<EthnicGroup,Integer>ethnicPop){
        this.precinctId = precinctId;
        this.state = state;
        this.neighbors = neighbors;
        this.countyId = countyId;
        this.totalPop = totalPop;
        this.white = ethnicPop.get(EthnicGroup.WHITE);
        this.black = ethnicPop.get(EthnicGroup.BLACK);
        this.asian = ethnicPop.get(EthnicGroup.ASIAN);
        this.hispanic = ethnicPop.get(EthnicGroup.HISPANIC);
        this.american_indian = ethnicPop.get(EthnicGroup.NATIVE_AMERICAN);
        this.native_hawaiian = ethnicPop.get(EthnicGroup.PACIFIC_ISLANDER);
        this.other = ethnicPop.get(EthnicGroup.OTHER);
    }
    
    public String getPrecinctId(){
        return precinctId;
    }
    public void setPrecinctId(String precinctId){
        this.precinctId = precinctId;
    }
    
    public int getCountyId(){
        return countyId;
    }
    public void setCountyId(int countyId){
        this.countyId = countyId;
    }
   
    public int getTotalPop(){
        return totalPop;
    }
    public void setTotalPop(int totalPop){
        this.totalPop = totalPop;
    }
    
    public StateName getState(){
        return state;
    }
    public void setState(StateName state){
        this.state = state;
    }
    
    public HashMap<EthnicGroup,Integer> getEthnicPop(){
        HashMap<EthnicGroup,Integer> ethnicPop = new HashMap<EthnicGroup,Integer>();
        ethnicPop.put(EthnicGroup.WHITE,white);
        ethnicPop.put(EthnicGroup.BLACK,black);
        ethnicPop.put(EthnicGroup.ASIAN,asian);
        ethnicPop.put(EthnicGroup.HISPANIC,hispanic);
        ethnicPop.put(EthnicGroup.NATIVE_AMERICAN,american_indian);
        ethnicPop.put(EthnicGroup.PACIFIC_ISLANDER,native_hawaiian);
        ethnicPop.put(EthnicGroup.OTHER,other);
        return ethnicPop;
    }
    public void setEthnicPop(HashMap<EthnicGroup,Integer> ethnicPop){
        this.white = ethnicPop.get(EthnicGroup.WHITE);
        this.black = ethnicPop.get(EthnicGroup.BLACK);
        this.asian = ethnicPop.get(EthnicGroup.ASIAN);
        this.hispanic = ethnicPop.get(EthnicGroup.HISPANIC);
        this.american_indian = ethnicPop.get(EthnicGroup.NATIVE_AMERICAN);
        this.native_hawaiian = ethnicPop.get(EthnicGroup.PACIFIC_ISLANDER);
        this.other = ethnicPop.get(EthnicGroup.OTHER);
    }

    // public ArrayList<Neighbors> getNeighbors(){
    //     return neighbors;
    // }
    // public void setNeighbors(ArrayList<Neighbors> neighbors){
    //     this.neighbors = neighbors;
    // }
}
