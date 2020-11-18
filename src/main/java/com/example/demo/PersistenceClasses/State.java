package com.example.demo.PersistenceClasses;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import com.example.demo.EnumClasses.*;

@Entity
public class State {
    @Id
    private int stateId;
    @Enumerated(EnumType.STRING)
    private StateName state;
    private String districtPath;
    private String precinctPath;
    private String heatMapPath;

    @OneToMany
    @JoinColumn(name = "state_id")
    @MapKey(name = "geoId")
    private Map<String, Precinct> precincts;

    public State(){
        // needed for JPA
    }

    public State(int stateId, StateName state, String districtPath, String precinctPath, String heatMapPath){
        this.stateId = stateId;
        this.state = state;
        this.districtPath = districtPath;
        this.precinctPath = precinctPath;
        this.heatMapPath = heatMapPath;
    }
    public int getStateId(){
        return stateId;
    }
    public StateName getStateName(){
        return state;
    }
    public String getDistrictPath(){
        return districtPath;
    }
    public String getHeatMapPath(){
        return heatMapPath;
    }
    public String getPrecinctPath(){
        return precinctPath;
    }

    public Map<String, Precinct> getPrecincts(){
        return precincts;
    }

    public Precinct getPrecinct(String geoId){
        return precincts.get(geoId);
    }
}
