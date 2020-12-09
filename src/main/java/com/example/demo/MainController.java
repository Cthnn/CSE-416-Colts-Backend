package com.example.demo;

import org.json.simple.JSONArray;
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

import java.util.Arrays;
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
        return jh.createJob(params);
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
    @GetMapping("/test")
    public void test(){
        try{
            //ServerDispatcher.cancelJob(412228);
            ServerDispatcher.retrieveResults(100);
            System.out.println("Successful Retrieval");
            ServerDispatcher.removeFiles(100);
            System.out.println("Successful Removal");
        }catch (Exception e){
            System.out.println("Unsuccesful");
        }
        //SeawulfHelper.getStatus(412157);
    }
    @GetMapping("/")
    public void home(){
        Job j = new Job();
        j.setJobId(2);
        j.setState(sh.getState(StateName.ALABAMA));
        j.setEthnicGroup(EthnicGroup.ASIAN);
        List<Districting> districtings = jh.initJobDistrictings(j);
        
        for(Districting dist: districtings){
            System.out.println("ds: " + dist.getDistricts().size());
            for(District d: dist.getDistricts()){
                System.out.println("d " + d.getDistrictId() + " : "+ d.getPrecincts().size());
            }
        }
        double[][] out = j.getBoxPlotValues();
        System.out.println("BOXPLOT");
        for(int i =0; i < out.length; i++){
            System.out.println(Arrays.toString(out[i]));
        }

        // jh.generateGeoJson(j,DistrictingType.AVERAGE,districtings.get(0));


        // Districting dist = sh.getState(StateName.ALABAMA).getDistricting();
        // System.out.println(dist.getDistrictingId());

        // float[] out = dist.getDistrictVAPPercentages(EthnicGroup.WHITE);
        // System.out.println(out.length);
        // // for(int i = 0; i < out.length; i++){
        // //     System.out.println(out[i]);
        // // }
        // return dist.getDistricts();


        // System.out.println(dist.getDistricts().get(0).getDistrictId());
        // System.out.println(dist.getDistricts().get(0).getPrecincts().size());
        // JobParams params = new JobParams();
        // params.comp = 0.25;
        // params.plans = 2345;
        // params.group = EthnicGroup.NATIVE_AMERICAN;
        // params.state = StateName.VIRGINIA;
        // params.pop = 0.5;
        // int jobId = jh.createJob(sh.getState(params.state).getStateId(), params);
        // jh.updateStatus(jobId, JobStatus.COMPLETED);
        // return "Colts Server";
    }
}