package com.example.remindme.Models;

import java.io.Serializable;

public class Elderly implements Serializable {
    private String name;
    private String id;

    public Elderly(){
        //required empty constructor
    }

    public Elderly(String name) {
        //this.id = id;
        this.name = name;
    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setID(String ID) {
//        this.id = ID;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}