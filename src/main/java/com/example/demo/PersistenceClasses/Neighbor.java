package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Neighbor {

    @Id
    private String geoId;
    private String neighborId;

    public String getGeoId(){
        return geoId;
    }

    public String getNeighborId(){
        return neighborId;
    }
}
