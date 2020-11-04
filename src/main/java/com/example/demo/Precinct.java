package com.example.demo;
import java.util.ArrayList;
import java.util.HashMap;
public class Precinct {
    public int precinctId;
    public StateName state;
    public ArrayList<float[]> neighbors; 
    public int countyId;
    public int totalPop;
    public HashMap<EthnicGroup,Integer> ethnicPop;
    
    public Precinct(int precinctId, StateName state, ArrayList<float[]> neighbors,int countyId,int totalPop,HashMap<EthnicGroup,Integer>ethnicPop){
        this.precinctId = precinctId;
        this.state = state;
        this.neighbors = neighbors;
        this.countyId = countyId;
        this.totalPop = totalPop;
        this.ethnicPop = ethnicPop;
    }
    
    public int getPrecinctId(){
        return precinctId;
    }
    public void setPrecinctId(int precinctId){
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
    
    public HashMap<EthnicGroup,Integer> ethnicPop(){
        return ethnicPop;
    }
    public void setEthnicPop(HashMap<EthnicGroup,Integer> ethnicPop){
        this.ethnicPop = ethnicPop;
    }

    public ArrayList<float[]> getNeighbors(){
        return neighbors;
    }
    public void setNeighbors(ArrayList<float[]> neighbors){
        
    }
}
