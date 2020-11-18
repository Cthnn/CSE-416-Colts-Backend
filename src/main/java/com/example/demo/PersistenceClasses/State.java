package com.example.demo.PersistenceClasses;

import java.util.Map;

import javax.persistence.Column;
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
    private int stateId;
    private StateName state;
    private String districtPath;
    private String precinctPath;
    private String heatMapPath;
    private Map<String, Precinct> precincts;

    public State(){/* needed for JPA */}

    public State(int stateId, StateName state, String districtPath, String precinctPath, String heatMapPath){
        this.stateId = stateId;
        this.state = state;
        this.districtPath = districtPath;
        this.precinctPath = precinctPath;
        this.heatMapPath = heatMapPath;
    }
    @Id
    public int getStateId(){
        return stateId;
    }
    public void setStateId(int id){
        stateId = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public StateName getStateName(){
        return state;
    }
    public void setStateName(StateName s){
        state = s;
    }

    public String getDistrictPath(){
        return districtPath;
    }
    public void setDistrictPath(String path){
        districtPath = path;
    }
    public String getHeatMapPath(){
        return heatMapPath;
    }
    public void setHeatMapPath(String path){
        heatMapPath = path;
    }
    public String getPrecinctPath(){
        return precinctPath;
    }
    public void setPrecinctPath(String path){
        precinctPath = path;
    }

    @OneToMany
    @JoinColumn(name = "state_id")
    @MapKey(name = "geoId")
    public Map<String, Precinct> getPrecincts(){
        return precincts;
    }

    public void setPrecincts(Map<String, Precinct> p){
        precincts = p;
    }

    public Precinct getPrecinct(String geoId){
        return precincts.get(geoId);
    }
}
