package com.example.remindme.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;

   public User(){
       //required empty constructor
   }


    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}