package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Neighbor {

    @Id
    public String geoId;
    public String neighborId;
}
