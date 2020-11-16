package com.example.demo.Handlers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.example.demo.EnumClasses.*;
import com.example.demo.PersistenceClasses.Precinct;
import com.example.demo.PersistenceClasses.State;
import com.example.demo.Repositories.PrecinctRepository;
import com.example.demo.Repositories.StateRepository;
@Service
public class StateHandler{
    private HashMap<StateName, State> states = new HashMap<>();
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private PrecinctRepository precinctRepository;

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

    private void initStates(){
        ArrayList<State> repoStates = (ArrayList<State>) stateRepository.findAll();
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

    public FileSystemResource getDistricts(StateName s){
        Path path = Paths.get(states.get(s).getDistrictPath());
        return new FileSystemResource(path); 
    }
    public FileSystemResource getPrecincts(StateName s){
        Path path = Paths.get(states.get(s).getPrecinctPath());
        return new FileSystemResource(path); 
    }
    public FileSystemResource getHeatMap(StateName s){
        Path path = Paths.get(states.get(s).getHeatMapPath());
        return new FileSystemResource(path); 
    }

    public Precinct getDemographic(String precinctId){
        Optional<Precinct> precinct = precinctRepository.findById(precinctId);
        if(precinct.isPresent()){
            return precinct.get();
        }

        return null;
    }
}