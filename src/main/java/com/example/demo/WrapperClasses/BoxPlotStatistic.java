package com.example.demo.WrapperClasses;

import java.util.Arrays;

public class BoxPlotStatistic {
    public double min = 0;
    public double percentile1 = 0;
    public double average = 0;
    public double percentile3 = 0;
    public double max = 0;
    public double total = 0;
    public int count = 0;

    public BoxPlotStatistic(double[] vaps){
        Arrays.sort(vaps);
        this.min = vaps[0];
        this.max = vaps[vaps.length-1];
        for(double vap : vaps){
            this.total += vap;
            this.count ++;
        }

        this.percentile1 = vaps[(int) Math.floor(0.25 * vaps.length)];
        this.percentile3 = vaps[(int) Math.floor(0.75 * vaps.length)];
        
        this.average = this.total/this.count;
    }

    public double[] toArray(){
        double[] output = new double[5];
        output[0] = min;
        output[1] = percentile1;
        output[2] = average;
        output[3] = percentile3;
        output[4] = max;

        return output;
    }
}