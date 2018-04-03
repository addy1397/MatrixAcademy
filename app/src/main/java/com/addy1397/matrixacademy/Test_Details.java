package com.addy1397.matrixacademy;

/**
 * Created by adity on 05-01-2018.
 */

public class Test_Details {
    private String Average;
    private String Date;
    private String Marks;
    private String Position;
    private String Total;
    private String Total_Number;

    public Test_Details(){

    }

    public Test_Details(String Average, String Date, String Marks,String Position, String Total ,String Total_Number){
        this.Average = Average;
        this.Date = Date;
        this.Marks = Marks;
        this.Position = Position;
        this.Total = Total;
        this.Total_Number = Total_Number;
    }

    public String getAverage(){
        return Average;
    }

    public void setAverage(String Average){
        this.Average = Average;
    }

    public String getDate(){
        return Date;
    }

    public void setDate(String Date){
        this.Date = Date;
    }

    public String getMarks(){
        return Marks;
    }

    public void setMarks(String Marks){
        this.Marks = Marks;
    }

    public String getPosition(){
        return Position;
    }

    /*public void setposition(String Position){
        this.Position = Position;
    }*/

    public String getTotal(){
        return Total;
    }

    public void setTotal(String Total){
        this.Total = Total;
    }

    public String getTotal_Number(){
        return Total_Number;
    }

    public void setTotal_Number(String Total_Number){
        this.Total_Number = Total_Number;
    }
}
