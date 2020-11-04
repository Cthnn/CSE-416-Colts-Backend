package com.example.demo;
import java.util.ArrayList;
import org.json.simple.JSONObject;

public class StateHandler{
    public ArrayList<State> states = new  ArrayList<State>();
    public JSONObject getDistricts(State s){
        return s.toJSON();
    }
    public JSONObject getPrecincts(State s){
        return s.toJSON();
    }
    public JSONObject getDemographic(State s,int precinctId){
        return s.toJSON();
    }
    public JSONObject getHeatMap(State s,EthnicGroup eg){
        return s.toJSON();
    }
}