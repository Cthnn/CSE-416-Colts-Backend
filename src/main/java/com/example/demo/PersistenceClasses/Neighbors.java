package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Neighbors {

    @Id
    @GeneratedValue
    public int id;
    public String gisId;
    public String neighbor;
}
