/**
 *
 * @author   :   gwencabangon
 * @IDNumber :   12119839
 * @unitCode :   COIT20245
 */

import java.util.Scanner;
import javax.swing.JOptionPane;

public class TaxTest
{   //main method begins program execution
    public static void main(String[] args)
    {
        
        Scanner input = new Scanner(System.in);     //declare a Scanner object for taking salary input
        final int N = 9;                            //declaring number of employees based on the highest number in author's ID number
        final double SALARY_RANGE_1 = 0;            //salary limit for tax group 1
        final double SALARY_RANGE_2 = 18200;        //salary limit for tax group 2
        final double SALARY_RANGE_3 = 37000;        //salary limit for tax group 3
        final double SALARY_RANGE_4 = 87000;        //salary limit for tax group 4
        final double SALARY_RANGE_5 = 180000;       //salary limit for tax group 5
        //initializing employee count per group
        int taxGroup_1 = 0;                         
        int taxGroup_2 = 0;
        int taxGroup_3 = 0;
        int taxGroup_4 = 0;
        int taxGroup_5 = 0;
        int counter = 1;                            //inital value of loop counter
        double taxableIncome = 0;                   //initializing variable where salary input will be stored
        
        //declaring object from Tax class
        Tax taxCalc = new Tax( taxableIncome , taxGroup_1 , taxGroup_2 , taxGroup_3 , taxGroup_4 , taxGroup_5 );
        
        //welcome message
        JOptionPane.showMessageDialog(null , "COIT20245 Assignment1:\nTax Computation"); //welcome message
        
        //start of loop
        while( counter <= N )
        {
            //prompt to enter input
            System.out.printf("Enter the taxable income for employee %d: ", counter);
            //read and assing input
            taxableIncome = input.nextDouble();
            
            //determine tax group of entered salary
            if(taxableIncome >= SALARY_RANGE_1 && taxableIncome <= SALARY_RANGE_2)
            {
                ++taxGroup_1; //add to tax group count if condition is met
            }
            //taxable income $18201 - $37000
            else if ( taxableIncome > SALARY_RANGE_2 && taxableIncome <= SALARY_RANGE_3 )
            {
                ++taxGroup_2;
            }

            //taxable income $37001 - $87000
            else if ( taxableIncome > SALARY_RANGE_3 && taxableIncome <= SALARY_RANGE_4 )
            {
                ++taxGroup_3;
            }

            //taxble income $87001 - $ 180000
            else if ( taxableIncome > SALARY_RANGE_4 && taxableIncome <= SALARY_RANGE_5 )
            {
                ++taxGroup_4;
            }   

            //taxable income $180001 and over
            else if ( taxableIncome > SALARY_RANGE_5 )
            {
                ++taxGroup_5;
            }
            
            //call highestTaxGroup method and add group count (in the parameter) to be processed
            taxCalc.highestTaxGroup( taxGroup_1 , taxGroup_2 , taxGroup_3 , taxGroup_4 , taxGroup_5 );
            
            //print calculated income tax by calling calculateTax method
            System.out.printf("The income tax for employee %d is $%.2f\n\n", counter , taxCalc.calculateTax(taxableIncome) );
            //call methods that determine highest and lowest
            taxCalc.setHighest();
            taxCalc.setLowest();
            
            //increment loop counter
            ++counter;
            
        }
        
        System.out.println("--------------------------------------------------Report-----------------------------------------------------");
            
        System.out.printf("Highest Tax: $%.2f\n", taxCalc.getHighestTax() ); //print highest tax
        System.out.printf("Lowest Tax: $%.2f\n", taxCalc.getLowestTax() ); //print lowest tax
        System.out.printf("Number of employees in Group 1: %d\n", taxGroup_1 ); //print group 1 count
        System.out.printf("Number of employees in Group 5: %d\n", taxGroup_5 ); //print group 5 count
        //call highestTaxGroup to determine grop with the highest count
        System.out.printf("Tax group number with the highest number of employees: %d \n", taxCalc.highestTaxGroup( taxGroup_1 , taxGroup_2 , taxGroup_3 , taxGroup_4 , taxGroup_5 ) );
        
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        
        System.out.println("---------------------------------------------End of Java Application-----------------------------------------");
        System.out.println("By: Gwen Cabangon"); //exit message
        System.out.println("ID: 12119839"); //exit message
    }
}//end of class TaxTest
