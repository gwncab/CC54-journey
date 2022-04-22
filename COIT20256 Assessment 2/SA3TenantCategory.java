
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: SA3TenantCategory.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */

public class SA3TenantCategory extends Person {
    //initialise location income and income source
    private Location location;
    private WEEKLY_INCOME income;
    private String incomeSource;
    
   /* // constructor
    public SA3TenantCategory(AGE age, GENDER gender, Location location, String income, String incomeSource){
        super(age, gender);
        this.location = location;
        this.income = WEEKLY_INCOME.getIncome(income);
        this.incomeSource = incomeSource;
    }
    */
        // constructor
    public SA3TenantCategory(AGE age, GENDER gender, Location location, WEEKLY_INCOME income, String incomeSource){
        super(age, gender);
        this.location = location;
        this.income = income;
        this.incomeSource = incomeSource;
    }
    
    //copy constructor
    public SA3TenantCategory(SA3TenantCategory another){
        this(another.getAge(), another.getGender(), another.getLocation(), 
                another.getIncome(), another.getIncomeSource());
    }

    // set location
    public void setLocation(Location location){
        this.location = location;
    }
    
    //return lcoation
    public Location getLocation(){
        return this.location;
    }
    
    //set income category
    public void setIncome(WEEKLY_INCOME income){
        this.income = income;
    }
    
    //return income category
    public WEEKLY_INCOME getIncome(){
        return income;
    }
    
    //set income source
    public void setIncomeSource(String incomeSource){
        this.incomeSource = incomeSource;
    }
    
    //return income source
    public String getIncomeSource(){
        return this.incomeSource;
    }
    
    //override to string
    @Override
    public String toString(){
        return String.format("%-5s %-5s %s %-5s %-5s",
                getLocation(), getIncome(), super.getGender(), super.getAge(), getIncomeSource());
    }


}
