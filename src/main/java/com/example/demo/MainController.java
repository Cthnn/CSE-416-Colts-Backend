package com.example.demo;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.EnumClasses.*;
import com.example.demo.PersistenceClasses.*;
import com.example.demo.WrapperClasses.*;
import com.example.demo.Handlers.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@CrossOrigin
public class MainController {
    @Autowired
    public JobHandler jh;
    @Autowired
    public StateHandler sh;
    private State state;

    @PostMapping("/state")
    public String setState(@RequestBody StateName s) {
        System.out.println("Setting state: " + s.toString());
        state = sh.getState(s);
        return "setState Success";
    }

    @GetMapping("/History")
    public List<Job> getHistory() {
        // System.out.println(sh.getDemographic(StateName.ALABAMA,
        // "G010009011").getDemographic().getTotalPopulation());
        return jh.getHistory();
    }

    @PostMapping("/createJob")
    public int createJob(@RequestBody JobParams params) {
        // params.put("state", stateName);
        return jh.createJob(params);
    }

    @PostMapping("/jobGeo")
    public Resource getJobGeo(@RequestBody JobGeoParams params) {
        System.out.println("Sending job geo");
        System.out.println(params.jobId + " " + params.type.toString());
        return jh.getJobGeo(params.jobId, params.type);
    }

    @PostMapping("/cancel")
    public String cancelJob(@RequestBody int jobId) {
        jh.cancelJob(jobId);
        return "200 OK";
    }

    @PostMapping("/statuses")
    public List<Job> getStatuses(@RequestBody int[] jobIds) {
        Integer[] convertedIds = IntStream.of(jobIds).boxed().toArray(Integer[]::new);
        return jh.getStatuses(convertedIds);
    }

    @PostMapping("/getBoxPlot")
    public double[][] getBoxPlot(@RequestBody int jobId) {
        System.out.println("BOXPLOT FOR " + jobId);
        double[][] out = jh.getBoxPlot(jobId);
        for(int i =0; i < out.length; i++){
            System.out.println(Arrays.toString(out[i]));
        }
        return out;
    }

    @PostMapping("/district")
    @ResponseBody
    public Resource getDistrict(@RequestBody StateName s) {
        System.out.println("Sending District data");
        return sh.getDistricts(s);
    }

    @PostMapping("/precinct")
    @ResponseBody
    public Resource getPrecinct(@RequestBody StateName s) {
        System.out.println("Sending Precinct data");
        return sh.getPrecincts(s);
    }

    @PostMapping("/demographic")
    public Precinct getDemographic(@RequestBody DemographicParams params) {
        return sh.getDemographic(params.state, params.precinctId);
    }

    @GetMapping("/summary/{jobId}")
    public Resource getSummary(@PathVariable("jobId") int jobId) {
        return jh.getSummary(jobId);
    }

    @GetMapping("/test")
    public void test() {
        try {
            // ServerDispatcher.cancelJob(412228);
            ServerDispatcher.retrieveResults(2);
            System.out.println("Successful Retrieval");
            // ServerDispatcher.removeFiles(100);
            // System.out.println("Successful Removal");
        } catch (Exception e) {
            System.out.println("Unsuccesful");
        }
        // SeawulfHelper.getStatus(412157);
    }

    @GetMapping("/")
    public void home() {
        // Job j = new Job();
        // j.setJobId(3);
        // j.setCompactness(0.5);
        // j.setPopulationDeviation(0.5);
        // j.setPlans(8);
        // j.setState(sh.getState(StateName.ALABAMA));
        // j.setEthnicGroup(EthnicGroup.ASIAN);

        // // try {
        // //     int slurmId = ServerDispatcher.initiateJob(j);
        // //     System.out.println(slurmId);
        // // } catch (IOException e) {
        // //     e.printStackTrace();
        // // }
        // List<Districting> districtings = jh.initJobDistrictings(j);
        //jh.updateStatus(j, JobStatus.COMPLETED);
        Job j = jh.getJob(3);
        List<Districting> districtings = j.getDistrictings();
        System.out.println("averageIndex" + j.getAverageDistrictingIndex());
        System.out.println("extremeIndex" + j.getExtremeDistrictingIndex());
        for(Districting dist: districtings){
            System.out.println("ds: " + dist.getDistricts().size());
            for(District d: dist.getDistricts()){
                System.out.println("d " + d.getDisplayNumber() + " : "+ d.getPrecincts().size());
            }
        }
        double[][] out = j.getBoxPlotValues();
        System.out.println("BOXPLOT");
        for(int i =0; i < out.length; i++){
            System.out.println(Arrays.toString(out[i]));
        }

        // jh.generateGeoJson(j,DistrictingType.AVERAGE,j.getAverageDistricting());
        // jh.generateGeoJson(j,DistrictingType.EXTREME,j.getExtremeDistricting());

        //jh.generateSummary(j);
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