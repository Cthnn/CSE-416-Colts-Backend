package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.Id;


import org.json.simple.JSONObject;
import com.example.demo.EnumClasses.*;

@Entity
public class State {
    @Id
    public int stateId;
    public StateName state;
    public String filePath;

    public State(StateName state){
        this.state = state;
    }
    
    public StateName getState(){
        return state;
    }
    public void setName(StateName state){
        this.state = state;
    }
    public JSONObject getHeatMap(EthnicGroup eg){
        return new JSONObject();
    }
    public JSONObject toJSON(){
        //possibly a redundant method
        return new JSONObject();
    }
}
