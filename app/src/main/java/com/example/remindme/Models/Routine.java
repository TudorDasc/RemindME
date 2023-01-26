package com.example.remindme.Models;

import java.util.ArrayList;

public class Routine {

    private ArrayList<String> steps;
    private String routineName;
    private String fileName;
    private String step1;
    private String step2;
    private String step3;

    public Routine() {
        // required empty public constructor
    }

    public Routine(String routineName, String fileName, ArrayList<String> steps, String step1,
                   String step2, String step3){
        this.routineName = routineName;
        this.fileName = fileName;
        this.step1 = step1;
        this.step2 = step2;
        this.step3 = step3;
        this.steps = new ArrayList<>();
    }

    public String getRoutineName(){
        return routineName;
    }

    public void setRoutineName(String name){
        this.routineName = routineName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getStep1(){
        return step1;
    }

    public void setStep1(String step1){
        this.step1 = step1;
    }

    public String getStep2(){
        return step2;
    }

    public void setStep2(String step1){
        this.step2 = step2;
    }

    public String getStep3(){
        return step3;
    }

    public void setStep3(String step1){
        this.step3 = step3;
    }
}
