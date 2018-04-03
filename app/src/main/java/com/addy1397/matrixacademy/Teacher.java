package com.addy1397.matrixacademy;

import android.media.Image;

/**
 * Created by adity on 05-01-2018.
 */

public class Teacher {

    public String name;
    public String biodata;
    public String Specialization;
    public String image;

    public Teacher(){

    }

    public Teacher(String name, String biodata, String Specialization,String image){
        this.name = name;
        this.biodata = biodata;
        this.Specialization = Specialization;
        this.image = image;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getBiodata(){
        return biodata;
    }

    public void setBiodata(String biodata){
        this.biodata = biodata;
    }

    public String getSpecialization_(){
        return Specialization;
    }

    public void setSpecialization_(String Specialization){
        this.Specialization = Specialization;
    }

    public String getImage(){return image;}

    public void setImage(String image){
        this.image = image;
    }
}
