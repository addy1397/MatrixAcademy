package com.addy1397.matrixacademy;

/**
 * Created by adity on 01-01-2018.
 */

/**
 * Created by adity on 31-12-2017.
 */

public class Feed {
    public String date;
    public String title;
    public String description;
    public String image;

    public Feed(){

    }

    public Feed(String date, String title, String description, String image){
        this.date = date;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
