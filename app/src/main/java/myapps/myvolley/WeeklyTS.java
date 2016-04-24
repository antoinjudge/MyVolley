package myapps.myvolley;

/**
 * Created by Antoin on 18/04/2016.
 */
public class WeeklyTS {
    //private variables
    int id;
    int basic;
    int overtime;
    int meals;
    int mileage;
    String date;

    // Empty constructor
    public WeeklyTS(){

    }
    // constructor
    public WeeklyTS(int id, int basic, int overtime,int meals, int mileage, String date){
        this.id = id;
        this.basic = basic;
        this.overtime = overtime;
        this.meals= meals;
        this.mileage = mileage;
        this.date = date;
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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date= date;
    };


    // setting name
    public int getBasic(){
        return this.basic;
    }
    public void setBasic(int basic){
        this.basic = basic;
    }

    // getting overtime
    public int getOvertime(){
        return this.overtime;
    }

    public void setOvertime(int overtime){
        this.overtime = overtime;
    }
    //get meals
    public int getMeals(){
      return this.meals;
    }
    public void  setMeals(int meals){
        this.meals= meals;
    }
    //get mileage
    public int getMileage(){
        return  this.mileage;
    }
    public  void setMileage(int mileage){
        this.mileage= mileage;
    }
}

