package com.example.demo.PersistenceClasses;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.example.demo.EnumClasses.*;

@Entity
public class Precinct {
    @Id
    private String geoId;
    private int stateId;
    private int countyId;
    private String precinctId;

    private String stateName;
    private String countyName;
    private String precinctName;

    private int totalPopulation;
    private int totalWhite;
    private int totalBlack;
    private int totalAsian;
    private int totalHispanic;
    private int totalAmericanIndian;
    private int totalNativeHawaiian;
    private int totalOther;

    private int vapPopulation;
    private int vapWhite;
    private int vapBlack;
    private int vapAsian;
    private int vapHispanic;
    private int vapAmericanIndian;
    private int vapNativeHawaiian;
    private int vapOther;

    @Transient
    private StateName state;

    @OneToMany
    @Transient
    @JoinColumn(name = "geo_id")
    private ArrayList<Neighbor> neighbors;

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
        return totalPopulation;
    }
    public void setTotalPop(int totalPop){
        this.totalPopulation = totalPop;
    }
    
    public StateName getState(){
        return state;
    }
    public void setState(StateName state){
        this.state = state;
    }
    
    public HashMap<EthnicGroup,Integer> getEthnicPop(){
        HashMap<EthnicGroup,Integer> ethnicPop = new HashMap<EthnicGroup,Integer>();
        ethnicPop.put(EthnicGroup.WHITE,vapWhite);
        ethnicPop.put(EthnicGroup.BLACK,vapBlack);
        ethnicPop.put(EthnicGroup.ASIAN,vapAsian);
        ethnicPop.put(EthnicGroup.HISPANIC,vapHispanic);
        ethnicPop.put(EthnicGroup.NATIVE_AMERICAN,vapAmericanIndian);
        ethnicPop.put(EthnicGroup.PACIFIC_ISLANDER,vapNativeHawaiian);
        ethnicPop.put(EthnicGroup.OTHER,vapOther);
        return ethnicPop;
    }
    // public void setEthnicPop(HashMap<EthnicGroup,Integer> ethnicPop){
    //     this.white = ethnicPop.get(EthnicGroup.WHITE);
    //     this.black = ethnicPop.get(EthnicGroup.BLACK);
    //     this.asian = ethnicPop.get(EthnicGroup.ASIAN);
    //     this.hispanic = ethnicPop.get(EthnicGroup.HISPANIC);
    //     this.american_indian = ethnicPop.get(EthnicGroup.NATIVE_AMERICAN);
    //     this.native_hawaiian = ethnicPop.get(EthnicGroup.PACIFIC_ISLANDER);
    //     this.other = ethnicPop.get(EthnicGroup.OTHER);
    // }

    public ArrayList<Neighbor> getNeighbors(){
        return neighbors;
    }
    public void setNeighbors(ArrayList<Neighbor> neighbors){
        this.neighbors = neighbors;
    }
}
