package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Neighbors {
    @Id
    public String gisId;
    @Id
    public String neighbor;
}
