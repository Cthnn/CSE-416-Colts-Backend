package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import com.example.demo.EnumClasses.*;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.WrapperClasses.*;
import com.example.demo.Handlers.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

@RestController
@CrossOrigin
public class MainController{
    @Autowired
    public JobHandler jh;
    public StateHandler sh = new StateHandler();
    private StateName stateName;

    @PostMapping("/state")
    public String setState(@RequestBody StateName s){
        System.out.println("Setting state: " + s.toString());
        stateName = s;
        return "setState Success";
    }
    @GetMapping("/History")
    public ArrayList<Job> getHistory(){
        return jh.getHistory();
    }
    @PostMapping("/createJob")
    public int createJob(@RequestBody JSONObject params){
        params.put("state", stateName);
        System.out.println(params.toString());
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
    @PostMapping("/statuses")
    public ArrayList<Job> getStatuses(@RequestBody int[] jobIds){
        Integer[] convertedIds = IntStream.of(jobIds).boxed().toArray( Integer[]::new );
        return jh.getStatuses(convertedIds);
    }
    @PostMapping("/genSummary")
    public double[][] genSummary(@RequestBody int jobId){
        return jh.genSummary(jobId);
    }
    @PostMapping("/district")
    public JSONObject getDistrict(@RequestBody StateName s){
        return sh.getDistricts(s);
    }
    @PostMapping("/precinct")
    @ResponseBody
    public FileSystemResource getPrecinct(@RequestBody StateName s){
        System.out.println("Sending Precinct data");
        return sh.getPrecincts(stateName);
    }
    @PostMapping("/demographic")
    public JSONObject getDemographic(@RequestBody DemoParams params){
        return sh.getDemographic(params.s, params.pId);
    }
    @PostMapping("/heatmap")
    @ResponseBody
    public FileSystemResource getHeatMap(@RequestBody StateName s){
        System.out.println("Sending HeatMap data");
        return sh.getHeatMap(stateName);
    }
    @GetMapping("/")
    public String home(){
        return "Colts Server";
    }
}