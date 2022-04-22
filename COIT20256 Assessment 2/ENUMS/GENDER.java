/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: GENDER.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public enum GENDER {
    
    //declare constants of enum type
    DEFAULT ("--"),
    MALE    ("Male"),
    FEMALE  ("Female");
    
    //instance field
    private String gender;
        
    //enum constructor
    GENDER(String gender){
        this.gender = gender;
    }
    
    public GENDER getMale(){
        return MALE;
    }
    
    public GENDER getFemale(){
        return FEMALE;
    }
    
    public static GENDER getGender(String value){
        GENDER gender = null;
        switch(value){
            case "Male": gender = MALE; break;
            case "Female": gender = FEMALE; break;
        }
        
        return gender;
    }
    
    //return gender
    public String getGender(){
        return gender;
    }
    
    //toString returns String gender
    @Override
    public String toString(){
        return gender;
    }
}
