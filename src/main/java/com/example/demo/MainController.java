package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@CrossOrigin
public class MainController{
    public JobHandler jh = new JobHandler();
    public StateHandler sh = new StateHandler();
    public SeawulfHelper swh = new SeawulfHelper();
    @PostMapping("/state")
    public String setState(@RequestBody State s){
        //Set the HTTP Sesssion's state
        return "setState Success";
    }
    @GetMapping("/History")
    public JSONObject getHistory(){
        return jh.getHistory();
    }
    @PostMapping("/createJob")
    public String createJob(@RequestBody String s){
        JSONParser parser = new JSONParser();
        JSONObject params;
        try{
            params = (JSONObject)parser.parse(s);
        }catch(Exception e){
            return "ERROR";
        }
        jh.createJob(params);
        return "200 OK";
    }
    @PostMapping("/jobGeo")
    public JSONObject getJobGeo(@RequestBody String params){
        int jobId = 0;
        DistrictingType type = null;
        return jh.getJobGeo(jobId,type);
    }
    @PostMapping("/cancel")
    public String cancelJob(@RequestBody int jobId){
        jh.cancelJob(jobId);
        return "200 OK";
    }
    @PostMapping("/delete")
    public String deleteJob(@RequestBody int jobId){
        jh.deleteJob(jobId);
        return "200 OK";
    }
    @PostMapping("/statuses")
    public JSONObject getStatuses(@RequestBody int[] jobIds){
        return jh.getStatuses(jobIds);
    }
    @PostMapping("/genSummary")
    public String genSummary(@RequestBody String params){
        int jobId = 0;
        Precinct[] precincts = null;
        jh.genSummary(jobId, precincts);
        return "200 OK";
    }
    @PostMapping("/district")
    public JSONObject getDistrict(@RequestBody State s){
        return sh.getDistricts(s);
    }
    @PostMapping("/precinct")
    public JSONObject getPrecinct(@RequestBody State s){
        return sh.getPrecincts(s);
    }
    @PostMapping("/demographic")
    public JSONObject getDemographic(@RequestBody String params){
        State s = null;
        int pID = 0;
        //Split params to state and precinctId;
        
        return sh.getDemographic(s, pID);
    }
    @PostMapping("/heatmap")
    public JSONObject getHeatMap(@RequestBody String params){
        State s = null;
        EthnicGroup eg = null;
        //Split params to state and eg;
        return sh.getHeatMap(s, eg);
    }
    @GetMapping("/")
    public String home(){
        return "Colts Server";
    }
}