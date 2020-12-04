package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
        //System.out.println(sh.getDemographic(StateName.ALABAMA, "G010009011").getDemographic().getTotalPopulation());
        return jh.getHistory();
    }
    @PostMapping("/createJob")
    public int createJob(@RequestBody JobParams params){
        // params.put("state", stateName);
        System.out.println(params.toString());
        return jh.createJob(sh.getState(params.state).getStateId(), params);
    }
    @PostMapping("/jobGeo")
    public Resource getJobGeo(@RequestBody JobGeoParams params){
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
    @PostMapping("/getSummary")
    public Resource getSummary(@RequestBody int jobId){
        return jh.getSummary(jobId);
    }
    @PostMapping("/getBoxPlot")
    public double[][] getBoxPlot(@RequestBody int jobId){
        return jh.getBoxPlot(jobId);
    }
    @PostMapping("/district")
    @ResponseBody
    public Resource getDistrict(@RequestBody StateName s){
        System.out.println("Sending District data");
        return sh.getDistricts(s);
    }
    @PostMapping("/precinct")
    @ResponseBody
    public Resource getPrecinct(@RequestBody StateName s){
        System.out.println("Sending Precinct data");
        return sh.getPrecincts(s);
    }
    @PostMapping("/demographic")
    public Precinct getDemographic(@RequestBody DemographicParams params){
        return sh.getDemographic(params.state, params.precinctId);
    }
    @PostMapping("/heatmap")
    @ResponseBody
    public Resource getHeatMap(@RequestBody StateName s){
        System.out.println("Sending HeatMap data");
        return sh.getHeatMap(s);
    }
    @GetMapping("/")
    public String home(){
        JobParams params = new JobParams();
        params.comp = 0.25;
        params.plans = 2345;
        params.group = EthnicGroup.NATIVE_AMERICAN;
        params.state = StateName.VIRGINIA;
        params.pop = 0.5;
        int jobId = jh.createJob(sh.getState(params.state).getStateId(), params);
        jh.updateStatus(jobId, JobStatus.COMPLETED);
        return "Colts Server";
    }
}