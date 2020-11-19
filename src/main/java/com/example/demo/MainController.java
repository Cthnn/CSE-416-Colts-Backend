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
import java.util.List;
import java.util.stream.IntStream;

@RestController
@CrossOrigin
public class MainController{
    @Autowired
    public JobHandler jh;
    @Autowired
    public StateHandler sh;
    private State state;

    @PostMapping("/state")
    public String setState(@RequestBody StateName s){
        System.out.println("Setting state: " + s.toString());
        state = sh.getState(s);
        return "setState Success";
    }
    @GetMapping("/History")
    public List<Job> getHistory(){
        // System.out.println(sh.getDemographic(StateName.ALABAMA, "G010009011").getNeighbors().size());
        return jh.getHistory();
    }
    @PostMapping("/createJob")
    public int createJob(@RequestBody JobParams params){
        // params.put("state", stateName);
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
    public List<Job> getStatuses(@RequestBody int[] jobIds){
        Integer[] convertedIds = IntStream.of(jobIds).boxed().toArray( Integer[]::new );
        return jh.getStatuses(convertedIds);
    }
    @PostMapping("/genSummary")
    public double[][] genSummary(@RequestBody int jobId){
        return jh.genSummary(jobId);
    }
    @PostMapping("/district")
    @ResponseBody
    public FileSystemResource getDistrict(@RequestBody StateName s){
        System.out.println("Sending District data");
        return sh.getDistricts(state.getStateName());
    }
    @PostMapping("/precinct")
    @ResponseBody
    public FileSystemResource getPrecinct(@RequestBody StateName s){
        System.out.println("Sending Precinct data");
        return sh.getPrecincts(state.getStateName());
    }
    @PostMapping("/demographic")
    public Precinct getDemographic(@RequestBody String precinctId){
        return sh.getDemographic(state.getStateName(), precinctId);
    }
    @PostMapping("/heatmap")
    @ResponseBody
    public FileSystemResource getHeatMap(@RequestBody StateName s){
        System.out.println("Sending HeatMap data");
        return sh.getHeatMap(state.getStateName());
    }
    @GetMapping("/")
    public String home(){
        return "Colts Server";
    }
}