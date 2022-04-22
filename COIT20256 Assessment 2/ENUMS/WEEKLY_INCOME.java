/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * File name: WEEKLY_INCOME.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public enum WEEKLY_INCOME {
    
    //declare constant of enum type weekly income
    NilIncome   ("Negative/Nil income"),
    below$400   ("$1-$399"),
    below$600   ("$400-$599"),
    below$1000  ("$600-$999");
    
    // instance field
    private String income;
    
    // enum constructor
    WEEKLY_INCOME(String incomeDesc){
        this.income = incomeDesc;
    }
    
    public static WEEKLY_INCOME getNilIncome(){
        return NilIncome;
    }
    
    public static WEEKLY_INCOME getBelow$400(){
        return below$400;
    }
    
    public static WEEKLY_INCOME getBelow$600(){
        return below$600;
    }
    
    public static WEEKLY_INCOME getBelow$1000(){
        return below$1000;
    }
    
    public static WEEKLY_INCOME getIncome(String incomeRange){
        WEEKLY_INCOME income = null;
        switch(incomeRange){
            case "Negative/Nil income": income = NilIncome ; break;
            case "$1-$399": income = below$400; break;
            case "$400-$599": income = below$600; break;
            case "$600-$999": income = below$1000; break;
        }
        
        return income;
    }
    
    // return income description
    public String getIncome(){
        return income;
    }
    
    @Override
    public String toString(){
        return income;
    }
}
