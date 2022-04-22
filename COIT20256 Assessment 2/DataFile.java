
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: DateFile.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public class DataFile {

    private Location loc;
    private LinkedList<Location> locationList = new LinkedList<>();
    private LinkedList<RiskyPersons> riskyPersons = new LinkedList<>();
    private Scanner input;
    private DatabaseUtility db;
    
    //executes read file methods
    public void fileRead(){
        openFile();
        readFile();
        closeFile();
    }
    
    //open file futureDemand.csv
    public void openFile()
    {
        
        try{
            //input = new ObjectInputStream(Files.newInputStream(Paths.get("futureDemand.csv")));
            input = new Scanner(Paths.get("futureDemand.csv"));
            
        }
        catch (IOException ioException){
            System.err.println("Error opening file.\nTerminating...\n");
            System.err.println("In openFile()\n"); // test if error in this method is being encountered
            System.exit(1);
        }
    }
    
    //read file content for Location
    public void readFile()
    {
        String line;
        AGE age;
        String genderRead[] = new String[25], 
                ageRead[] = new String[25],
                incomeSource[] = new String[25],
                code,
                locName;
        GENDER gender; //initial
        
        String column[] = new String[25];
        
        SA3TenantCategory tenant;
        
        int count = 0;
        try
        {
            //while there is to read
            while (input.hasNext())
            {
                line = input.nextLine();
                if (count == 0)
                 genderRead = line.split(","); //gender
                else if (count == 1)
                  ageRead = line.split(","); //age
                else if (count == 2)
                  incomeSource = line.split(","); //employed,
                else{
                  column = line.split(",");
                  
                  //skip 3 lines
                  for(int j=3, i=3; j<genderRead.length; j++, i++){
                      if(genderRead[j].trim().equals("Female"))
                          gender = GENDER.FEMALE;
                      else gender = GENDER.MALE;
                      
                      //get location 
                      loc = new Location(Integer.parseInt(column[0].strip()), column[1]);
                      locationList.add(loc);
                      
                      //get tenant object
                      tenant = new SA3TenantCategory(AGE.getAgeCategory(ageRead[j].strip()),
                        gender, loc, WEEKLY_INCOME.getIncome(column[2]), incomeSource[i] );
                      
                      //add to riskyPersons arraylist
                      riskyPersons.add(new RiskyPersons(tenant, Integer.parseInt(column[i]) ));
                  }
              }
                count++;
            }
            
        }
        
        catch(NoSuchElementException elementException)
        {
            System.err.println("File improperly formed. Terminating... from read()");
        }
        catch(IllegalStateException illegalStateException)
        {
            System.err.println("Error reading file. Terminating... from read()");
        }
        /*catch(NumberFormatException numberFormatException)
        {
            System.err.println("numberFormatException");
        }*/
    }
    
    //close file
    public void closeFile()
    {
        
        if (input != null)
            input.close();
      
    }
    
    //get locationList to initialise sa3code dropdown
    public LinkedList<Location> passLocationList(){
       
        return locationList;
    }
    
    //get riskypersonsList to initialise risky list in controller
    public LinkedList<RiskyPersons> passRiskyPersonsList(){
         return riskyPersons;
    }
}

