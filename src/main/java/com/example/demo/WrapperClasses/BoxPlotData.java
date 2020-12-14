package com.example.demo.WrapperClasses;

public class BoxPlotData {
    public double[][] summaryData;
    public double[] enactedData;
    public double[] averageData;
    public double[] extremeData;

    public BoxPlotData(double[][] summarData, double[] enactedData, double[] averageData, double[] extremeData){
        this.summaryData = new double[summarData.length][];
        for(int i=0;i<summarData.length;i++){
            this.summaryData[i] = toPercent(summarData[i]);
        }

        this.enactedData = toPercent(enactedData);
        this.averageData = toPercent(averageData);
        this.extremeData = toPercent(extremeData);

    }

    private double[] toPercent(double[] data){
        double[] out = new double[data.length];
        for(int i=0;i<data.length;i++){
            out[i] = data[i] * 100;
        }

        return out;
    }
}