package com.example.demo.PersistenceClasses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "neighbor")
public class Neighbor {

    private String geoId;
    private String neighborId;

    @Id
    @Column(name = "geo_id")
    public String getGeoId(){
        return geoId;
    }
    @Column(name = "neighbor_id")
    public String getNeighborId(){
        return neighborId;
    }

    public void setGeoId(String id){
        geoId = id;
    }

    public void setNeighborId(String id){
        neighborId = id;
    }
}
