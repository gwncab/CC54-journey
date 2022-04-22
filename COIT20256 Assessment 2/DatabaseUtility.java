/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.io.*;
import java.util.*;
/**
 * File name: DatabaseUtility.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public class DatabaseUtility {
    private AGE age;
    private GENDER gender;
    private WEEKLY_INCOME income;
    private Location loc;
    private RiskyPersons riskyPersons;
    private final  String MYSQL_URL  ;
    final  String DB_URL;
    private Connection sqlConnection,dbConnection;
    private Statement  statement;
    private final String dbCreateSQL;
    private final String USER_NAME;
    private final String PASSWORD;
    private final String TABLE_LOCATIONS_QRY;
    private final String TABLE_RISKYPERSONS_QRY;
    private final String TABLE_AGE_QRY;
    private final String TABLE_WEEKLYINCOME_QRY;
    //private final String SELECT_AUTHORS_QRY;
    private DatabaseMetaData dbmd;
    int locationId;
    private LinkedList<Location> dataLocation = new LinkedList<>();
    private LinkedList<RiskyPersons> dataRiskyPersons = new LinkedList<>();
    private LinkedList<String> checkForDuplicateAge = new LinkedList<>();
    private LinkedList<String> checkForDuplicateIncome = new LinkedList<>();
    private LinkedList<Integer> checkForDuplicateCode = new LinkedList<>();
    private LinkedList<SA3TenantCategory> SA3CategoryList = new LinkedList<>();
    
    public DatabaseUtility(){
        MYSQL_URL = "jdbc:mysql://localhost:3306";
        DB_URL = MYSQL_URL +"/HomelessInfo";
        //initialise MySql usename and password 
        USER_NAME ="root";
        PASSWORD = "admin";
        
        statement = null;
        //sql query to create database.

        dbCreateSQL = "CREATE DATABASE HomelessInfo";
        //sql queries to create Tables
        TABLE_LOCATIONS_QRY = "CREATE TABLE LOCATIONS "+
                                                                       "(LocationId INTEGER not NULL AUTO_INCREMENT," +
                                                                          "locationCode INTEGER(5)," +
                                                                          "locationName VARCHAR(50),"  +                                                
                                                                          "PRIMARY KEY (LocationId) "
                + "                                                         )";
        TABLE_RISKYPERSONS_QRY = "CREATE TABLE RISKYPERSONS "+
                                                                 "(RiskyPersonsId INTEGER not NULL AUTO_INCREMENT," +
                                                                  "AgeId INTEGER NOT NULL,"+
                                                                  "Gender VARCHAR(50) NOT NULL," +
                                                                  "WeeklyIncomeId INTEGER NOT NULL,"+
                                                                  "IncomeSource VARCHAR(20) NOT NULL," +
                                                                  "PersonCount INTEGER NOT NULL,"+
                                                                  "LocationID INTEGER NOT NULL,"+
                                                                  "PRIMARY KEY (RiskyPersonsId),"+
                                                                   "FOREIGN KEY (LocationId) REFERENCES LOCATIONS "+
                                                                   "(LocationId) ," +
                                                                   "FOREIGN KEY (AgeId) REFERENCES AGE (AgeId) "+
                                                                   ", "+
                                                                   "FOREIGN KEY (WeeklyIncomeId) REFERENCES WEEKLYINCOME (WeeklyIncomeId) "+
                                                                   " )";
        TABLE_AGE_QRY = "CREATE TABLE AGE "+
                                                                   "(AgeId INTEGER not NULL AUTO_INCREMENT," +
                                                                   "AgeRange VARCHAR(20) not NULL," +
                                                                   "PRIMARY KEY (AgeId))";
        TABLE_WEEKLYINCOME_QRY = "CREATE TABLE WEEKLYINCOME "+
                                                                   "(WeeklyIncomeId INTEGER not NULL AUTO_INCREMENT," +
                                                                   "WeeklyIncome VARCHAR(50) not NULL," +
                                                                   "PRIMARY KEY (WeeklyIncomeId) )";
                                                                             
      
       /*SELECT_AUTHORS_QRY = "SELECT AuthorId, firstName, lastName FROM AUTHORS";*/
    }
    
    public boolean createDBtables(){
        boolean dbExists = false, tblLocationExist = false, 
                tblRiskyPersonsExist = false,
                tblAgeExist = false, 
                tblWeeklyIncomeExist = false;
                //dbCreated = false;
        
        String databaseName = "";
        //Register MySql database driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            
            return false;
        }
        System.out.println("MySQL JDBC Driver Registered!");
        //connect to MySql ;
        try {
            sqlConnection = DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD);
            statement = sqlConnection.createStatement();
        }
        catch (SQLException e) {
            System.out.println("SQL Connection Failed! Check output console");
            e.printStackTrace();
            return false;
        }
        //check whether the databse exists.
        
        try {
            //get the list of databases
            ResultSet dbData = sqlConnection.getMetaData().getCatalogs();

            //iterate each catalog in the ResultSet 
            while (dbData.next()) {
                // Get the database name, which is at position 1
                databaseName = dbData.getString(1);
                // Test print of database names, can be removed
                // System.out.printf("%s ",databaseName);  
                if (databaseName.equalsIgnoreCase("HomelessInfo") )
                    dbExists = true;
            }
            
            //if database doesn't exist create database executing the query.
            if (! dbExists) {
                statement.executeUpdate(dbCreateSQL);
                System.out.println("Database successfully created...");
            }
            
            //close the existing connection to connect to MySql
            if (sqlConnection != null){
                sqlConnection.close();
            }
            
            //connect to HomelessInfo database
            dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            statement = dbConnection.createStatement();
            dbmd= dbConnection.getMetaData();
             
            // loop through the list of tables if the tables are already created
            ResultSet rs = dbmd.getTables(null, null, "%", null);
            
            while (rs.next()) {
                if((rs.getString(4).equalsIgnoreCase("LOCATIONS")))
                    tblLocationExist = true;
                if((rs.getString(4).equalsIgnoreCase("AGE")))
                    tblAgeExist = true;
                if((rs.getString(4).equalsIgnoreCase("WEEKLYINCOME")))
                    tblWeeklyIncomeExist = true;
                if((rs.getString(4).equalsIgnoreCase("RISKYPERSONS")))
                    tblRiskyPersonsExist = true;
            }
            
            //if any of the tables doesn't exist create table executing the query
            if (!tblLocationExist){
                statement.executeUpdate(TABLE_LOCATIONS_QRY);
                System.out.println("Locations table created.");
            }
            if (!tblAgeExist){
                statement.executeUpdate(TABLE_AGE_QRY);
                System.out.println("Age table created.");
            }
            if (!tblWeeklyIncomeExist){
                statement.executeUpdate(TABLE_WEEKLYINCOME_QRY); 
                System.out.println("WeeklyIncome table created.");
            }
            if (!tblRiskyPersonsExist){
                statement.executeUpdate(TABLE_RISKYPERSONS_QRY);
                System.out.println("RiskyPersons table created.");
            }

        }
        
        catch (SQLException e) {
            System.out.println("*****************FROM CREATE*****************");
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
        
        return true;
    
    }
    
    public LinkedList<String> getCurrentAgeDb(){
        ResultSet getAge;
        
        try{
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
           statement = dbConnection.createStatement();
           getAge = statement.executeQuery("SELECT ageRange FROM AGE");
           while (getAge.next()){
               String tmp = getAge.getString("agerange");
               
               checkForDuplicateAge.add(tmp);
           }
           
           
            
        }
        catch(SQLException e){
             System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            
        }
        
        return checkForDuplicateAge;
    }
    
    public void insertAge(LinkedList<RiskyPersons> riskyPersonsList){
        PreparedStatement addAge;
        
        try{
            //connect to database
            System.out.println("Connecting to database from insertage");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
           if(getCurrentAgeDb().isEmpty())
               checkForDuplicateAge = new LinkedList<>();
           else
               checkForDuplicateAge = getCurrentAgeDb();
           
           
            for(RiskyPersons r : riskyPersonsList){
                String value = r.getSA3Category().getAge().toString();
                
                if(checkForDuplicateAge.contains(value)) {
                    continue;
                }
                else{
                    //Insert risky persons to riskypersons table with appropriate LocationID
                    addAge = dbConnection.prepareStatement("INSERT INTO AGE " +
                                                               "(ageRange) " +
                                                                "VALUES (?)");
                    
                    addAge.setString(1, r.getSA3Category().getAge().getAgeRange());
                    addAge.executeUpdate();
                    checkForDuplicateAge.add(value);
                }
            }
        }
        catch(SQLException e){
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return;
        }
    }
    
    public LinkedList<String> getCurrentWeeklyIncomeDb(){
        ResultSet getWeeklyIncome;
        
        try{
            //connect to database
            System.out.println("Connecting to database from getCurrentWeeklyIncomeDb");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
           statement = dbConnection.createStatement();
           getWeeklyIncome = statement.executeQuery("SELECT weeklyincome FROM WEEKLYINCOME");
           while (getWeeklyIncome.next()){
               String tmp = getWeeklyIncome.getString("weeklyincome");
               
               checkForDuplicateIncome.add(tmp);
           }
           
           
            
        }
        catch(SQLException e){
             System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            
        }
        
        return checkForDuplicateIncome;
    }
    
    public void insertWeeklyIncome(LinkedList<RiskyPersons> riskyPersonsList){
        PreparedStatement addWeeklyIncome;
        
        try{
            //connect to database
            System.out.println("Connecting to database from insertWeeklyIncome");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
            //Link list to contain weekly income and filter for duplicates
            if(getCurrentWeeklyIncomeDb().isEmpty())
                checkForDuplicateIncome = new LinkedList<>();
            else
                checkForDuplicateIncome = getCurrentWeeklyIncomeDb();
            
            for(RiskyPersons r : riskyPersonsList){
                income = r.getSA3Category().getIncome();
                
                if(checkForDuplicateIncome.contains(income.getIncome())) {
                    continue;
                }
                else{
                    addWeeklyIncome = dbConnection.prepareStatement("INSERT INTO WEEKLYINCOME " +
                                                               "(weeklyincome) " +
                                                                "VALUES (?)");
                    
                    addWeeklyIncome.setString(1, r.getSA3Category().getIncome().getIncome());
                    addWeeklyIncome.executeUpdate();
                    checkForDuplicateIncome.add(income.getIncome());
                }
            }
            
        }
        catch(SQLException e){
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return;
        }
    }
    
    public LinkedList<Integer> getCurrentLocationDb(){  //public LinkedList<Integer> getCurrentLocationDb()
        ResultSet getLocationCode;
        checkForDuplicateCode.clear();
        
        try{
            //connect to database
            System.out.println("Connecting to database from getCurrentLocationDb");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
           statement = dbConnection.createStatement();
           getLocationCode = statement.executeQuery("SELECT locationcode FROM LOCATIONS");
           while (getLocationCode.next()){
               int tmpcode = getLocationCode.getInt("locationcode");
               //String tmpname = getLocationCode.getString("locationname");
               checkForDuplicateCode.add(tmpcode);
               //dataLocation.add(new Location(tmpcode, tmpname));
           }
            
        }
        catch(SQLException e){
             System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            
        }
        
        return checkForDuplicateCode;
    }
    
    public LinkedList<Location> getLocationListDb(){  //public LinkedList<Integer> getCurrentLocationDb()
        ResultSet getLocationList;
        dataLocation.clear();
        
        try{
            //connect to database
            System.out.println("Connecting to database from getCurrentLocationDb");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
           statement = dbConnection.createStatement();
           getLocationList = statement.executeQuery("SELECT locationcode, locationname FROM LOCATIONS");
           while (getLocationList.next()){
               int tmpcode = getLocationList.getInt("locationcode");
               String tmpname = getLocationList.getString("locationname");
               dataLocation.add(new Location(tmpcode, tmpname));
           }
            
        }
        catch(SQLException e){
             System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            
        }
        
        return dataLocation;
    }
    
    public void insertCSVLocation(LinkedList<Location> locationList) 
    {
      PreparedStatement addLocation;  
      try{
          //connect to database
          System.out.println("Connecting to database from insertCSVLocation");
          
          if (dbConnection  == null)
              dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
          
          if(getCurrentLocationDb().isEmpty())
                checkForDuplicateCode = new LinkedList<>();
            else
                checkForDuplicateCode = getCurrentLocationDb();
          
          for(Location i : locationList){
              if (checkForDuplicateCode.contains(i.getCode()))
                  continue;
              else {
                  addLocation  = dbConnection.prepareStatement("INSERT INTO Locations " +
                                                               "(locationCode, locationName)" +
                                                                "VALUES ( ?, ?)");
                  
                  
                  addLocation.setInt(1, i.getCode());
                  addLocation.setString(2, i.getName());
                  addLocation.executeUpdate();
                  System.out.println("locations csv added");
                  checkForDuplicateCode.add(i.getCode());
                  dataLocation.add(new Location(i.getCode(), i.getName()));
              }
          }
          //locationId++;
      }
      catch(SQLException e) {
          System.out.println("Connection Failed! Check output console");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          e.printStackTrace();
          return;
      }
    }
    
    public boolean insertLocation(LinkedList<Location> locationList) 
    {
        boolean locationsaved = false, exist = false;
        PreparedStatement addLocation;  
      try{
          //connect to database
          System.out.println("Connecting to database from insertLocation");
          
          if (dbConnection  == null){
              dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
              System.out.println("Success...");
          }
          
          System.out.println(locationList.getLast().getCode());
          checkForDuplicateCode = getCurrentLocationDb();
          for(Integer locn : checkForDuplicateCode){
              System.out.println("Matched code "+locn);
            if(locn == locationList.getLast().getCode()){
              exist = true;
            }
            else exist = false;
          }
          
          if(!exist){
              addLocation  = dbConnection.prepareStatement("INSERT INTO Locations " +
                                                                   "(locationCode, locationName)" +
                                                                    "VALUES ( ?, ?)");
              int lastCode = locationList.getLast().getCode();
              String lastName = locationList.getLast().getName();
              addLocation.setInt(1, lastCode);
              addLocation.setString(2, lastName);
              addLocation.executeUpdate();
              System.out.println("locations app added");
              locationsaved = true;
          }
          else locationsaved = false;
          
      }
      catch(SQLException e) {
          System.out.println("Connection Failed! Check output console");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          e.printStackTrace();
          //return;
      }
      
      return locationsaved;
    }
    
    public LinkedList<SA3TenantCategory> getSA3CategoryList(){ // public LinkedList<SA3TenantCategory> getSA3CategoryList()
        ResultSet getSA3CategoryList;
        
        try{
            //connect to database
            System.out.println("Connecting to database from getSA3CategoryList()...");
            if (dbConnection == null){
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                System.out.println("getSA3CategoryList() Connected!");
            }
           statement = dbConnection.createStatement();
           getSA3CategoryList = statement.executeQuery("SELECT ageRange, gender, locationcode, locationname, weeklyincome, incomesource FROM RISKYPERSONS AS R, "+
                                                    "AGE AS A, LOCATIONS AS L, WEEKLYINCOME AS W WHERE R.ageid = A.ageid AND R.locationid  = L.locationid AND "+
                                                    " R.weeklyincomeid = W.weeklyincomeid");
           while (getSA3CategoryList.next()){
               AGE selectAge = AGE.getAgeCategory(getSA3CategoryList.getString("ageRange").strip());
               GENDER selectGender = GENDER.getGender(getSA3CategoryList.getString("gender").strip());
               Location selectLocation = new Location(getSA3CategoryList.getInt("locationcode"), getSA3CategoryList.getString("locationname").strip());
               WEEKLY_INCOME selectWeeklyIncome = WEEKLY_INCOME.getIncome(getSA3CategoryList.getString("weeklyincome").strip());
               String selectIncomeSource = getSA3CategoryList.getString("incomesource").strip();
               
               SA3TenantCategory tmp = new SA3TenantCategory(selectAge, selectGender, selectLocation, selectWeeklyIncome, selectIncomeSource);
               SA3CategoryList.add(tmp);
           }
           
           System.out.println("SA3CategoryList populated");
            
        }
        catch(SQLException e){
             System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            
        }
        
        return SA3CategoryList;
    }
    
    public boolean insertRiskyPersons(LinkedList<RiskyPersons> riskyPersonsList){
        boolean riskypersonssaved = false;
        PreparedStatement addRiskyPersons, getcount = null;
        ResultSet getPersonCount;
        int count = 0;
        boolean sa3categoryexist = false;
        int assignLocationID = 0;
        int temporaryID = 1;
        
        try {
            //connect to database
            System.out.println("Connecting to database from insertRiskyPersons()...");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
            System.out.println(riskyPersonsList.getLast().getSA3Category());
            
            SA3CategoryList = getSA3CategoryList();
            //for(SA3TenantCategory category : SA3CategoryList){
                //System.out.println("Current category: "+category);
                if(SA3CategoryList.toString().contains(riskyPersonsList.getLast().getSA3Category().toString())){
                    sa3categoryexist = true;
                }
                else sa3categoryexist = false;
            //}
            
            if(sa3categoryexist){
                checkForDuplicateCode = getCurrentLocationDb();
                for(Integer code : checkForDuplicateCode){
                    if(code == riskyPersonsList.getLast().getSA3Category().getLocation().getCode())
                        assignLocationID = temporaryID;
                    temporaryID++;
                }
                temporaryID = 1;
                //assign age category id from age table
                int ageID = 0;
                switch(riskyPersonsList.getLast().getSA3Category().getAge()){
                    case age1:
                        ageID = 1;
                        break;
                    case age2:
                        ageID = 2;
                        break;
                    case age3:
                        ageID = 3;
                        break;
                    case age4:
                        ageID = 4;
                        break;
                }
                
                //assign income category id from weeklyincome table
                int incomeID = 0;
                switch(riskyPersonsList.getLast().getSA3Category().getIncome()){
                    case NilIncome:
                        incomeID = 1;
                        break;
                    case below$400:
                        incomeID = 2;
                        break;
                    case below$600:
                        incomeID = 3;
                        break;
                    case below$1000:
                        incomeID = 4;
                        break;
                }
                
                //extract personcount
                statement = dbConnection.createStatement();
                getPersonCount = statement.executeQuery("SELECT personcount from riskypersons WHERE "+
                                    " ageid =" + ageID + " and gender = '"+ riskyPersonsList.getLast().getSA3Category().getGender().getGender() + "' and " +
                                    " weeklyincomeid = "+ incomeID +" and incomesource = '"+ riskyPersonsList.getLast().getSA3Category().getIncomeSource()+"'"+
                                    " and locationid = "+assignLocationID);
                while(getPersonCount.next())
                    count = getPersonCount.getInt("personcount");
                
                addRiskyPersons = dbConnection.prepareStatement("UPDATE RISKYPERSONS SET personcount = ? WHERE ageid = ? AND gender = ? AND weeklyincomeid = ? AND incomesource = ? AND locationid = ?");
                
                //String ageString = i.getSA3Category().getAge().getAgeRange();
                String genderString = riskyPersonsList.getLast().getSA3Category().getGender().getGender();
                //String weeklyincome  = i.getSA3Category().getIncome().getIncome();
                String incomesource = riskyPersonsList.getLast().getSA3Category().getIncomeSource();
                addRiskyPersons.setInt(1, count+1);
                addRiskyPersons.setInt(2, ageID);
                addRiskyPersons.setString(3, genderString);
                addRiskyPersons.setInt(4, incomeID);
                addRiskyPersons.setString(5, incomesource);
                addRiskyPersons.setInt(6, assignLocationID);
                addRiskyPersons.executeUpdate();
                
                System.out.println("Tenant added!");
                riskypersonssaved = true;
            }
            else{
                System.out.println("Entering else statement...");
                checkForDuplicateCode = getCurrentLocationDb();
                for(Integer code : checkForDuplicateCode){
                    if(code == riskyPersonsList.getLast().getSA3Category().getLocation().getCode())
                        assignLocationID = temporaryID;
                    temporaryID++;
                }
                temporaryID = 1;
                //assign age category id from age table
                int ageID = 0;
                switch(riskyPersonsList.getLast().getSA3Category().getAge()){
                    case age1:
                        ageID = 1;
                        break;
                    case age2:
                        ageID = 2;
                        break;
                    case age3:
                        ageID = 3;
                        break;
                    case age4:
                        ageID = 4;
                        break;
                }
                
                //assign income category id from weeklyincome table
                int incomeID = 0;
                switch(riskyPersonsList.getLast().getSA3Category().getIncome()){
                    case NilIncome:
                        incomeID = 1;
                        break;
                    case below$400:
                        incomeID = 2;
                        break;
                    case below$600:
                        incomeID = 3;
                        break;
                    case below$1000:
                        incomeID = 4;
                        break;
                }
                
                addRiskyPersons = dbConnection.prepareStatement("INSERT INTO RISKYPERSONS " +
                                                               "(ageid, gender, weeklyincomeid, incomesource, personcount, locationid) " +
                                                                "VALUES (?, ?, ?, ?, ?, ?)");
                
                addRiskyPersons.setInt(1, ageID);
                addRiskyPersons.setString(2, riskyPersonsList.getLast().getSA3Category().getGender().getGender());
                addRiskyPersons.setInt(3, incomeID);
                addRiskyPersons.setString(4, riskyPersonsList.getLast().getSA3Category().getIncomeSource());
                addRiskyPersons.setInt(5, riskyPersonsList.getLast().getPersonCount());
                addRiskyPersons.setInt(6, assignLocationID);
                addRiskyPersons.executeUpdate();
                
                System.out.println("Tenant with new SA3Category added!");
                System.out.println("*************************************************************************");
                System.out.println("New Tenant age: "+riskyPersonsList.getLast().getSA3Category().getAge());
                System.out.println("New Tenant age id: "+ageID);
                System.out.println("New Tenant gender: "+riskyPersonsList.getLast().getSA3Category().getGender().getGender());
                System.out.println("New Tenant income: "+riskyPersonsList.getLast().getSA3Category().getIncome());
                System.out.println("New Tenant income id: "+incomeID);
                System.out.println("New Tenant income source: "+riskyPersonsList.getLast().getSA3Category().getIncomeSource());
                System.out.println("New Tenant location: "+riskyPersonsList.getLast().getSA3Category().getLocation());
                System.out.println("New Tenant location id: "+assignLocationID);
                riskypersonssaved = true;
            }
           
        }
         catch(SQLException e) {
          System.out.println("Connection Failed! Check output console");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          e.printStackTrace();
          //return;
      }
        return riskypersonssaved;
    }
    
    public void insertCSVRiskyPersons(LinkedList<RiskyPersons> riskyPersonsList){
        PreparedStatement addRiskyPersons;
        
        try {
            //connect to database
            System.out.println("Connecting to database...");
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
            //String duplicateChecker;
            for(RiskyPersons i : riskyPersonsList){
                int assignLocationID = 0;
                int temporaryID = 1;
                //compare locations from location linkedlist to locations from riskypersons
                // to assign appropriate locationID to RiskyPerson.locationID
                for(Location l : dataLocation){
                    System.out.println(l.getCode()+ " "+l.getName());
                    if(l.getCode() == i.getSA3Category().getLocation().getCode())
                        assignLocationID = temporaryID;
                    temporaryID++;
                }
                
                //assign age category id from age table
                int ageID = 0;
                switch(i.getSA3Category().getAge()){
	            case age1:
	            	ageID = 1;
	            	break;
	            case age2:
	            	ageID = 2;
	            	break;
	            case age3:
	            	ageID = 3;
	            	break;
	            case age4:
	            	ageID = 4;
	            	break;
                }
                
                //assign income category id from weeklyincome table
                int incomeID = 0;
                switch(i.getSA3Category().getIncome()){
                    case NilIncome:
                        incomeID = 1;
                        break;
                    case below$400:
                        incomeID = 2;
                        break;
                    case below$600:
                        incomeID = 3;
                        break;
                    case below$1000:
                        incomeID = 4;
                        break;
                }
                //Insert risky persons to riskypersons table with appropriate LocationID
                addRiskyPersons = dbConnection.prepareStatement("INSERT INTO RISKYPERSONS " +
                                                               "(ageid, gender, weeklyincomeid, incomesource, personcount, locationid) " +
                                                                "VALUES (?, ?, ?, ?, ?, ?)");
                
                //String ageString = i.getSA3Category().getAge().getAgeRange();
                String genderString = i.getSA3Category().getGender().getGender();
                //String weeklyincome  = i.getSA3Category().getIncome().getIncome();
                String incomesource = i.getSA3Category().getIncomeSource();
                int personcount = i.getPersonCount();
                
                addRiskyPersons.setInt(1, ageID);
                addRiskyPersons.setString(2, genderString);
                addRiskyPersons.setInt(3, incomeID);
                addRiskyPersons.setString(4, incomesource);
                addRiskyPersons.setInt(5, personcount);
                addRiskyPersons.setInt(6, assignLocationID);
                addRiskyPersons.executeUpdate();
                //SA3CategoryList.add(i.getSA3Category());
            }
            
        }
         catch(SQLException e) {
          System.out.println("Connection Failed! Check output console");
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          e.printStackTrace();
          return;
         }
    }
    
    public LinkedList<RiskyPersons> getRiskyPersonsList(){
        ResultSet getRiskyPersonsList;
        LinkedList<RiskyPersons> riskyPersonsList = new LinkedList<>();
        
        try{
            //connect to database
            System.out.println("Connecting to database from getSA3CategoryList()...");
            if (dbConnection == null){
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                System.out.println("getSA3CategoryList() Connected!");
            }
           statement = dbConnection.createStatement();
           getRiskyPersonsList = statement.executeQuery("SELECT ageRange, gender, locationcode, locationname, weeklyincome, incomesource, personcount FROM RISKYPERSONS AS R, "+
                                                    "AGE AS A, LOCATIONS AS L, WEEKLYINCOME AS W WHERE R.ageid = A.ageid AND R.locationid  = L.locationid AND "+
                                                    " R.weeklyincomeid = W.weeklyincomeid");
           
           while (getRiskyPersonsList.next()){
               AGE selectAge = AGE.getAgeCategory(getRiskyPersonsList.getString("ageRange").strip());
               GENDER selectGender = GENDER.getGender(getRiskyPersonsList.getString("gender").strip());
               Location selectLocation = new Location(getRiskyPersonsList.getInt("locationcode"), getRiskyPersonsList.getString("locationname").strip());
               WEEKLY_INCOME selectWeeklyIncome = WEEKLY_INCOME.getIncome(getRiskyPersonsList.getString("weeklyincome").strip());
               String selectIncomeSource = getRiskyPersonsList.getString("incomesource").strip();
               int personCount = getRiskyPersonsList.getInt("personcount");
               
               RiskyPersons tmp = new RiskyPersons(selectAge, selectGender, selectLocation, selectWeeklyIncome, selectIncomeSource, personCount);
               riskyPersonsList.add(tmp);
           }
           
           System.out.println("SA3CategoryList populated");
            
        }
        catch(SQLException e){
             System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return riskyPersonsList;
    }
     
}
