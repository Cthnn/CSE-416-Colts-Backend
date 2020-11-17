package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
}
