package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import com.example.demo.EnumClasses.*;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.WrapperClasses.*;
import com.example.demo.Handlers.*;
import java.util.ArrayList;
import java.util.stream.IntStream;
@RestController
@CrossOrigin
public class MainController{
    public JobHandler jh = new JobHandler();
    public StateHandler sh = new StateHandler();
    @PostMapping("/state")
    public String setState(State s){
        //Set the HTTP Sesssion's state
        return "setState Success";
    }
    @GetMapping("/History")
    public ArrayList<Job> getHistory(){
        return jh.getHistory();
    }
    @PostMapping("/createJob")
    public int createJob(@RequestBody String s){
        JSONParser parser = new JSONParser();
        JSONObject params;
        try{
            params = (JSONObject)parser.parse(s);
        }catch(Exception e){
            System.out.println("Error");
            return -1;
        }
        return jh.createJob(params);
    }
    @PostMapping("/jobGeo")
    public JSONObject getJobGeo(@RequestBody JobGeoParams params){
        return jh.getJobGeo(params.jobId,params.type);
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
    public ArrayList<Job> getStatuses(@RequestBody int[] jobIds){
        Integer[] convertedIds = IntStream.of(jobIds).boxed().toArray( Integer[]::new );
        return jh.getStatuses(convertedIds);
    }
    @PostMapping("/genSummary")
    public String genSummary(@RequestBody int jobId){
        jh.genSummary(jobId);
        return "200 OK";
    }
    @PostMapping("/district")
    public JSONObject getDistrict(@RequestBody StateName s){
        return sh.getDistricts(s);
    }
    @PostMapping("/precinct")
    public JSONObject getPrecinct(@RequestBody StateName s){
        return sh.getPrecincts(s);
    }
    @PostMapping("/demographic")
    public JSONObject getDemographic(@RequestBody DemoParams params){
        //Split params to state and precinctId;
        
        return sh.getDemographic(params.s, params.pId);
    }
    @PostMapping("/heatmap")
    public JSONObject getHeatMap(@RequestBody HeatMapParams params){
        return sh.getHeatMap(params.s, params.eg);
    }
    @GetMapping("/")
    public String home(){
        return "Colts Server";
    }
}