package com.example.demo.PersistenceClasses;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.demo.EnumClasses.*;

@Entity
public class Precinct {
    @Id
    private String geoId;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
    private int countyId;
    private String precinctId;

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

    @OneToMany
    @JoinColumn(name = "geo_id")
    private List<Neighbor> neighbors;

    public String getGeoId(){
        return geoId;
    }

    public State getState(){
        return state;
    }

    public String getPrecinctId(){
        return precinctId;
    }
    
    public int getCountyId(){
        return countyId;
    }

    public int getTotalPop(){
        return totalPopulation;
    }

    public int getVapPop(){
        return vapPopulation;
    }
    
    public String getCountyName(){
        return countyName;
    }

    public String getPrecinctName(){
        return precinctName;
    }

    public HashMap<EthnicGroup,Integer> getTotalPopulations(){
        HashMap<EthnicGroup,Integer> totalPop = new HashMap<EthnicGroup,Integer>();
        totalPop.put(EthnicGroup.WHITE,totalWhite);
        totalPop.put(EthnicGroup.BLACK,totalBlack);
        totalPop.put(EthnicGroup.ASIAN,totalAsian);
        totalPop.put(EthnicGroup.HISPANIC,totalHispanic);
        totalPop.put(EthnicGroup.NATIVE_AMERICAN,totalAmericanIndian);
        totalPop.put(EthnicGroup.PACIFIC_ISLANDER,totalNativeHawaiian);
        totalPop.put(EthnicGroup.OTHER,totalOther);
        return totalPop;
    }
    
    public HashMap<EthnicGroup,Integer> getVapPopulations(){
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
    
    public List<Neighbor> getNeighbors(){
        return neighbors;
    }
    public void setNeighbors(List<Neighbor> neighbors){
        this.neighbors = neighbors;
    }
}
