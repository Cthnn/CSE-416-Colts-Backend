package com.example.demo.PersistenceClasses;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.example.demo.EnumClasses.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class State {
    private int stateId;
    private StateName state;
    private Districting districting;
    private Map<String, Precinct> precincts;

    public State(){/* needed for JPA */}

    public State(int stateId, StateName state){
        this.stateId = stateId;
        this.state = state;
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

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "districting_id")
    public Districting getDistricting(){
        return districting;
    }

    public void setDistricting(Districting dist){
        districting = dist;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    @MapKey(name = "geoId")
    public Map<String, Precinct> getPrecincts(){
        return precincts;
    }

    public void setPrecincts(Map<String, Precinct> p){
        precincts = p;
    }

    @JsonIgnore
    @Transient
    public Precinct getPrecinct(String geoId){
        return precincts.get(geoId);
    }

    @JsonIgnore
    @Transient
    public int getNumDistricts(){
        return districting.getDistricts().size();
    }
}
