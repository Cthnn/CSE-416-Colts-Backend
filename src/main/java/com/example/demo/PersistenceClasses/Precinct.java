package com.example.demo.PersistenceClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.example.demo.EnumClasses.*;

@Entity
public class Precinct {
    private String geoId;
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

    private List<Neighbor> neighbors;

    @Id
    @Column(name = "geo_id")
    public String getGeoId(){ return geoId; }
    public void setGeoId(String id){ geoId = id; }

    @ManyToOne
    @JoinColumn(name = "state_id")
    public State getState(){ return state; }
    public void setState(State s){ state = s; }

    public String getPrecinctId(){ return precinctId; }
    public void setPrecinctId(String id){ precinctId = id; }
    
    public int getCountyId(){ return countyId; }
    public void setCountyId(int id){ countyId = id; }

    public String getCountyName(){ return countyName; }
    public void setCountyName(String name){ countyName = name; }

    public String getPrecinctName(){ return precinctName; }
    public void setPrecinctName(String name){ precinctName = name; }

    public int getTotalPopulation(){ return totalPopulation; }
    public void setTotalPopulation(int pop){ totalPopulation = pop; }

    public int getVapPopulation(){ return vapPopulation; }
    public void setVapPopulation(int pop){ vapPopulation = pop; }
    
    // -----------

    public int getTotalWhite(){ return totalWhite; }
    public void setTotalWhite(int pop){ totalWhite = pop; }

    public int getTotalBlack(){ return totalBlack; }
    public void setTotalBlack(int pop){ totalBlack = pop; }

    public int getTotalAsian(){ return totalAsian; }
    public void setTotalAsian(int pop){ totalAsian = pop; }

    public int getTotalHispanic(){ return totalHispanic; }
    public void setTotalHispanic(int pop){ totalHispanic = pop; }

    public int getTotalAmericanIndian(){ return totalAmericanIndian; }
    public void setTotalAmericanIndian(int pop){ totalAmericanIndian = pop; }

    public int getTotalNativeHawaiian(){ return totalNativeHawaiian; }
    public void setTotalNativeHawaiian(int pop){ totalNativeHawaiian = pop; }

    public int getTotalOther(){ return totalOther; }
    public void setTotalOther(int pop){ totalOther = pop; }


    //---------------------

    public int getVapWhite(){ return vapWhite; }
    public void setVapWhite(int pop){ vapWhite = pop; }

    public int getVapBlack(){ return vapBlack; }
    public void setVapBlack(int pop){ vapBlack = pop; }

    public int getVapAsian(){ return vapAsian; }
    public void setVapAsian(int pop){ vapAsian = pop; }

    public int getVapHispanic(){ return vapHispanic; }
    public void setVapHispanic(int pop){ vapHispanic = pop; }

    public int getVapAmericanIndian(){ return vapAmericanIndian; }
    public void setVapAmericanIndian(int pop){ vapAmericanIndian = pop; }

    public int getVapNativeHawaiian(){ return vapNativeHawaiian; }
    public void setVapNativeHawaiian(int pop){ vapNativeHawaiian = pop; }

    public int getVapOther(){ return vapOther; }
    public void setVapOther(int pop){ vapOther = pop; }

    @OneToMany
    @JoinColumn(name = "geo_id")
    public List<Neighbor> getNeighbors(){ return neighbors; }
    public void setNeighbors(List<Neighbor> neighbors){ this.neighbors = neighbors; }

    @Transient
    public Map<EthnicGroup,Integer> getTotalPopulations(){
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
    
    @Transient
    public Map<EthnicGroup,Integer> getVapPopulations(){
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
}
