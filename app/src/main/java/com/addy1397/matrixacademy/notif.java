package com.addy1397.matrixacademy;

/**
 * Created by adity on 01-01-2018.
 */

/**
 * Created by adity on 31-12-2017.
 */

public class notif {
    private String Date;
    private String Title;
    private String Description;

    public notif(){}

    public notif(String Date, String Title, String Description){
        this.Date = Date;
        this.Title = Title;
        this.Description = Description;
    }

    public String getDate(){
        return Date;
    }

    public void setDate(String date){
        this.Date = date;
    }

    public String getTitle(){
        return Title;
    }

    public void setTitle(String title){
        this.Title = title;
    }

    public String getDescription(){
        return Description;
    }

    public void setDescription(String description){
        this.Description = description;
    }
}
