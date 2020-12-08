package com.example.demo.PersistenceClasses;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.example.demo.EnumClasses.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Districting {
    private int districtingId;
    private DistrictingType type;

    private Set<District> districts;

    public Districting(){}

    public Districting(int districtingId, DistrictingType type){
        //this.districtingId = districtingId;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "districting_id")
    public int getDistrictingId(){
        return districtingId;
    }
    public void setDistrictingId(int districtingId){
        this.districtingId = districtingId;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "districting_id")
    public Set<District> getDistricts(){
        return districts;
    }
    public void setDistricts(Set<District> districts){
        this.districts = districts;
    }

    @Enumerated(EnumType.STRING)
    public DistrictingType getType(){
        return type;
    }

    public void setType(DistrictingType t){
        type = t;
    }

    @JsonIgnore
    @Transient
    public double[] getDistrictVAPPercentages(EthnicGroup eg){
        double[] res = new double[districts.size()];
        List<District> sortedDistricts = districts.stream().sorted((d1, d2) -> Double.compare(d1.getVAPPercentage(eg),d2.getVAPPercentage(eg))).collect(Collectors.toList());

        int i = 0;
        for(District dist : sortedDistricts){
            res[i] = dist.getVAPPercentage(eg);
            dist.setDisplayNumber(i+1);
            i++;
        }
        return res;
    }
}
