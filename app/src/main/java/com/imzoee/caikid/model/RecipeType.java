package com.imzoee.caikid.model;

/**
 * Created by zoey on 2016/5/12.
 *
 * Recipe type model, used to obtain the server side's types list.
 */
public class RecipeType {
    private int id;
    private String belong;
    private String name;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getBelong(){
        return belong;
    }

    public void setBelong(String belong){
        this.belong = belong;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
