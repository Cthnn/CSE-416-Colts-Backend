package com.example.demo.PersistenceClasses;

import java.util.ArrayList;
import com.example.demo.EnumClasses.*;

public class Districting {
    public int districtingId;
    public ArrayList<District> districts;
    public StateName state;
    public Districting(int districtingId, ArrayList<District> districts, StateName state){
        this.districtingId = districtingId;
        this.districts = districts;
        this.state = state;
    }
    public int getDistrictingId(){
        return districtingId;
    }
    public void setDistrictingId(int districtingId){
        this.districtingId = districtingId;
    }
    public ArrayList<District> getDistricts(){
        return districts;
    }
    public void setDistricts(ArrayList<District> districts){
        this.districts = districts;
    }
    public StateName getState(){
        return state;
    }
    public void setState(StateName state){
        this.state = state;
    }
    public void sortDistrics(EthnicGroup eg){
        //sort
    }
    public float[] getDistrictVAPs(EthnicGroup eg){
        float[] res = new float[districts.size()];
        for(int i =0; i< districts.size();i++){
            res[i] = districts.get(i).getVAP(eg);
        }
        return res;
    }
}
