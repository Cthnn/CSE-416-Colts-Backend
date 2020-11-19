package com.example.demo.PersistenceClasses;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(Neighbor.class)
@Table(name = "neighbor")
public class Neighbor implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String geoId;
    private String neighborId;

    @Id
    @Column(name = "geo_id")
    public String getGeoId(){
        return geoId;
    }
    @Id
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
