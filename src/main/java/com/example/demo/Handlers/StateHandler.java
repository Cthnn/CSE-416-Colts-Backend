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
import com.example.demo.WrapperClasses.PathBuilder;
@Service
public class StateHandler{
    private Map<StateName, State> states = new HashMap<>();
    @Autowired
    private StateRepository stateRepository;

    private void insertStates(){
        int[] stateIds = {1,12,51};
        for(int i = 0; i < StateName.values().length; i++){
            int id = stateIds[i];

            State state = new State(id, StateName.values()[i]);
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
        Path path = Paths.get(PathBuilder.getDistrictPath(s));
        return new FileSystemResource(path); 
    }
    public Resource getPrecincts(StateName s){
        Path path = Paths.get(PathBuilder.getPrecinctPath(s));
        return new FileSystemResource(path); 
    }
    public Resource getHeatMap(StateName s){
        Path path = Paths.get(PathBuilder.getHeatMapPath(s));
        return new FileSystemResource(path); 
    }

    public Precinct getDemographic(StateName s, String precinctId){
        return states.get(s).getPrecinct(precinctId);
    }

    public static StateName getState(int id){
        switch(id){
            case 1:
                return StateName.ALABAMA;
            case 12:
                return StateName.FLORIDA;
            case 51:
                return StateName.VIRGINIA;
        }

        return null;
    }
}