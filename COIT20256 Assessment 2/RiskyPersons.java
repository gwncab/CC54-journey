

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: RiskyPersons.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public class RiskyPersons {
    //initialise SA3Category object
    SA3TenantCategory SA3Category;
    int personCount;
    
    public void setSA3Category(SA3TenantCategory SA3Category){
        this.SA3Category = SA3Category;
    }
    
    public SA3TenantCategory getSA3Category(){
        return SA3Category;
    }
    
    public void setPersonCount(int personCount){
        this.personCount = personCount;
    }
    
    public int getPersonCount(){
        return personCount;
    }
    
    //constructor
    public RiskyPersons(SA3TenantCategory SA3Category, int personCount){
        this.SA3Category = SA3Category;
        this.personCount = personCount;
    }
    
    //constrctor for risky persons input
    public RiskyPersons(AGE age, GENDER gender, Location location, WEEKLY_INCOME income, 
            String incomeSource, int personCount){
        this.SA3Category = new SA3TenantCategory(age, gender, location, income, incomeSource);
        this.personCount = personCount;
    }
    
    public String toString(){
        return String.format("\n %s %d", this.SA3Category.toString(), this.personCount);
    }
    
}
