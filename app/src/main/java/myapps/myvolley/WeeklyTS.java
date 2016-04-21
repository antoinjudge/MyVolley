package myapps.myvolley;

/**
 * Created by Antoin on 18/04/2016.
 */
public class WeeklyTS {
    //private variables
    int id;
    int basic;
    int overtime;
    String date;

    // Empty constructor
    public WeeklyTS(){

    }
    // constructor
    public WeeklyTS(int id, int basic, int overtime, String date){
        this.id = id;
        this.basic = basic;
        this.overtime = overtime;
        this.date = date;
    }

    // constructor
    public WeeklyTS(int id, int basic, int overtime){
        this.id = id;
        this.basic = basic;
        this.overtime= overtime;
    }
    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    //setting dte
    public void setDate(String date) {this.date= date;};

    // getting name
    public int getBasic(){
        return this.basic;
    }

    // setting name
    public void setBasic(int basic){
        this.basic = basic;
    }

    // getting phone number
    public int getOvertime(){
        return this.overtime;
    }

    // setting phone number
    public void setOvertime(int overtime){
        this.overtime = overtime;
    }
}

