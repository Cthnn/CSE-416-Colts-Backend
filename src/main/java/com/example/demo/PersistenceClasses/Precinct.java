package com.example.demo.PersistenceClasses;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Precinct {
    private String geoId;
    private State state;
    private int countyId;
    private String precinctId;

    private String countyName;
    private String precinctName;

    private Demographic demographic;
    private List<Precinct> neighbors;

    @Id
    @Column(name = "geo_id")
    public String getGeoId(){ return geoId; }
    public void setGeoId(String id){ geoId = id; }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "state_id")
    public State getState(){ return state; }
    public void setState(State s){ state = s; }

    public String getPrecinctId(){ return precinctId; }
    public void setPrecinctId(String id){ precinctId = id; }
    
    public int getCountyId(){ return countyId; }
    public void setCountyId(int id){ countyId = id; }

    public String getCountyName(){ return countyName; }
    public void setCountyName(String name){ countyName = name; }

    public String getPrecinctName(){ return precinctName; }
    public void setPrecinctName(String name){ precinctName = name; }

    @OneToOne
    @JoinColumn(name = "geo_id")
    public Demographic getDemographic(){ return demographic; }
    public void setDemographic(Demographic demographic) { this.demographic = demographic; }

    @JsonIgnore
    @ManyToMany(/*fetch = FetchType.EAGER*/)
    @JoinTable(
        name = "neighbor",
        joinColumns = @JoinColumn(name = "geo_id"),
        inverseJoinColumns = @JoinColumn(name = "neighbor_id")
    )
    public List<Precinct> getNeighbors(){ return neighbors; }
    public void setNeighbors(List<Precinct> neighbors){ this.neighbors = neighbors; }
}
