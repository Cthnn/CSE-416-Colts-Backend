package com.example.demo.PersistenceClasses;

import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
                                          
import com.example.demo.EnumClasses.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class District {

    private int districtId;
    private int districtingId;
    private int displayNumber;
    private List<Precinct> precincts;

    public District() {}
    public District(int districtId, List<Precinct> precincts){
        this.districtId = districtId;
        this.precincts = precincts;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getDistrictId(){
        return districtId;
    }
    public void setDistrictId(int districtId){
        this.districtId = districtId;
    }
    
    @Column(name = "districting_id")
    public int getDistrictingId(){
        return districtingId;
    }
    public void setDistrictingId(int id){
        districtingId = id;
    }

    public int getDisplayNumber(){
        return displayNumber;
    }
    public void setDisplayNumber(int num){
        displayNumber = num;
    }
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "district_precinct",
        joinColumns = @JoinColumn(name = "district_id"),
        inverseJoinColumns = @JoinColumn(name = "precinct_geo_id")
    )
    public List<Precinct> getPrecincts(){
        return precincts;
    }
    public void setPrecincts(List<Precinct> precincts){
        this.precincts = precincts;
    }

    @Transient
    public double getVAPPercentage(EthnicGroup eg){
        int totalPop = 0;
        int egPop = 0;
        for(int i = 0; i < precincts.size();i++){
            Precinct p = precincts.get(i);
            totalPop += p.getDemographic().getTotalPopulation();
            egPop += p.getDemographic().getVapPopulations().get(eg);
        }
        return ((double)egPop)/totalPop;
    }

    @Transient
    public int getTotalPopulation(){
        int count = 0;
        for(int i = 0; i < precincts.size();i++){
            count += precincts.get(i).getDemographic().getTotalPopulation();
        }
        return count;
    }

    @Transient
    public int getTotalCounty(){
        HashSet<Integer> ids = new HashSet<Integer>();
        for(int i = 0; i < precincts.size();i++){
            ids.add(precincts.get(i).getCountyId());
        }
        return ids.size();
    }
}
