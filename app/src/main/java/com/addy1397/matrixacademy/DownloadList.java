package com.addy1397.matrixacademy;

/**
 * Created by adity on 20-01-2018.
 */

public class DownloadList {
    private String date;
    private String title;
    private String image;

    public DownloadList(){}

    public DownloadList(String date, String title, String image){
        this.date = date;
        this.title = title;
        this.image = image;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String gettitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getimage(){
        return image;
    }

    public void setimage(String description){
        this.image = image;
    }
}
