/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: AGE.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public enum AGE {
    
    age1("50-54"),
    age2("55-59"),
    age3("60-64"),
    age4("over 65");
    
    private String ageRange;

    
    private AGE(String ageRange){
        this.ageRange = ageRange;

    }
    
    public static AGE getAge1(){
        return age1;
    }
    
    public static AGE getAge2(){
        return age2;
    }
    
    public static AGE getAge3(){
        return age3;
    }
    
    public static AGE getAge4(){
        return age4;
    }
    
    public static AGE getAgeCategory(String ageRange){
        AGE age = null;
        switch(ageRange){
            case "50-54": age = age1; break;
            case "55-59": age = age2; break;
            case "60-64": age = age3; break;
            case "over 65": age = age4; break;
            
        }
        
        return age;
    }
    
    //return ageRange
    public String getAgeRange(){
        return ageRange;
    }
    
    //toString returns String ageRange
    @Override
    public String toString(){
        return String.format("%s", this.ageRange);
    }
}
