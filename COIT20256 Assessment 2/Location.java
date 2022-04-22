
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: Location.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public class Location 
{
    //initisliase code name location list and sa3code list
    private int code;
    private String name;
    private LinkedList<Location> location = new LinkedList<>();
    private LinkedList<Integer> sa3CodeList = new LinkedList<>();
    private final String codeLimit = "\\d{5}";
    private final String locNameLimit = "[A-Z][a-z]+([\\s-\\s][A-Z][a-z]+)*";
    private final String locNameAlpha = "[A-Z][a-z]*";
    String temp;
    
    //default constructor
    public Location()
    {
        this(0,"undefined");
    }
    
    //parametised constructor
    public Location(int code, String name)
    {
        this.code = code;
        this.name = name;
        
        sa3CodeList.add(code);
    }
    
    //copy constructor
    public Location(Location another)
    {
        this(another.code, another.name);
    }
    
    //accessor and mutator of code
    public void setCode(int code) throws InputMismatchException
    {
        temp = Integer.toString(code);
        if(temp.matches(codeLimit))
                this.code = code;
        else throw new InputMismatchException("Enter 5-digit SA3Code");
    }
    
    public int getCode()
    {
        return this.code;
    }
    
    //accessor and mutator of location name
    public void setName(String name) throws InputMismatchException
    {
        if(name.matches(locNameLimit) || name.matches(locNameAlpha))
            this.name = name;
        else throw new InputMismatchException("Invalid format.");
    }
    
    public String getName()
    {
        return name;
    }
    
    @Override
    public String toString()
    {
        return String.format("%d %s", this.getCode(), this.getName());
    }
    
    //store location code and location name in arraylist
    public void setLocationList(LinkedList<Location> location)
    {
        
        
        this.location = location;
    }
    
    public LinkedList<Location> getLocationList()
    {
        return location;
    }
    
    public LinkedList<Integer> getCodeList(LinkedList<Location> location)
    {
        for(Location i : location)
        {
            
            if(!sa3CodeList.contains(i.getCode()))
                sa3CodeList.add(i.getCode());
        }
        
        return sa3CodeList;
    }
    
}


