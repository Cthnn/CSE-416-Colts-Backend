package com.example.demo.PersistenceClasses;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Neighbors {
    @Id
    @Autowired
    public String gisId;
    @Id
    @Autowired
    public String neighbor;
}
