package com.example.demo.Handlers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.demo.EnumClasses.*;
import com.example.demo.PersistenceClasses.Precinct;
import com.example.demo.PersistenceClasses.State;
import com.example.demo.Repositories.StateRepository;
@Service
public class StateHandler{
    private Map<StateName, State> states = new HashMap<>();
    @Autowired
    private StateRepository stateRepository;

    private void insertStates(){
        int[] stateIds = {1,12,51};
        for(int i = 0; i < StateName.values().length; i++){
            String stateName = StateName.values()[i].toString().toLowerCase();
            int id = stateIds[i];
            String districtPath = "src/main/resources/districts/"+stateName+"_districts.json";
            String precinctPath = "src/main/resources/precincts/"+stateName+"_precincts.json";
            String heatmapPath = "src/main/resources/heatmaps/"+stateName+"_heatmap.json";

            State state = new State(id, StateName.values()[i], districtPath, precinctPath, heatmapPath);
            stateRepository.save(state);
            states.put(state.getStateName(), state);
        }
    }

    @PostConstruct
    private void initStates(){
        List<State> repoStates = (ArrayList<State>) stateRepository.findAll();
        if(repoStates.size() == 0){
            insertStates();
        }else{
            for(int i = 0; i < repoStates.size(); i++){
                State state = repoStates.get(i);
                states.put(state.getStateName(), state);
            }
        }
    }

    public State getState(StateName name){
        if(states.size() == 0)
            initStates();
            
        return states.get(name);
    }

    public Resource getDistricts(StateName s){
        Path path = Paths.get(states.get(s).getDistrictPath());
        return new FileSystemResource(path); 
    }
    public Resource getPrecincts(StateName s){
        Path path = Paths.get(states.get(s).getPrecinctPath());
        return new FileSystemResource(path); 
    }
    public Resource getHeatMap(StateName s){
        Path path = Paths.get(states.get(s).getHeatMapPath());
        return new FileSystemResource(path); 
    }

    public Precinct getDemographic(StateName s, String precinctId){
        return states.get(s).getPrecinct(precinctId);
    }
}