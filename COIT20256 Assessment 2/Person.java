/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: Person.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */

public abstract class Person
{
    //initialise age and gender
    private AGE age;
    private GENDER gender;
    
    //default constructor
    public Person()
    {
        this(null,null);
    }
    
    //parametised constructor
    public Person(AGE age, GENDER gender)
    {
        this.age = age;
        this.gender = gender;
    }
    
    //copy constructor
    public Person(Person another)
    {
        this(another.age, another.gender);
    }
    
    //set age category
    public void setAge(AGE age)
    {
        this.age = age;

    }
    
    //return age category
    public AGE getAge()
    {
        return this.age;
    }
    
    //set gender cateogory
    public void setGender(GENDER gender)
    {
        this.gender = gender;
    }
    
    //get gender category
    public GENDER getGender()
    {
        return this.gender;
    }
    
    //override to string
    @Override
    public abstract String toString();
    

}
