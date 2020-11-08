package com.example.demo.Handlers;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.EnumClasses.*;
public class StateHandler{
    public ArrayList<State> states = new  ArrayList<State>();
    public JSONObject getDistricts(StateName s){
        return null;
    }
    public JSONObject getPrecincts(StateName s){
        return null;
    }
    public JSONObject getDemographic(StateName s,int precinctId){
        return null;
    }
    public JSONObject getHeatMap(StateName s,EthnicGroup eg){
        return null;
    }
}