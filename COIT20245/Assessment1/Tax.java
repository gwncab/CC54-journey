/**
 *
 * @author   :   gwencabangon
 * @IDNumber :   12119839
 * @unitCode :   COIT20245
 */

public class Tax
{
    private double employeeIncome;          //declare variable to store employee salary to be taxed
    private double incomeTax = 0;           //initialize incomeTax
    private double highestTax = 0;          //initialize highest income tax computed
    int i = 0;                              //intial zero value
    private double lowestTax = i;           //initialize lowest income tax computed
    
    
    //set constant variables
    final double INCOME_RANGE_1 = 0;        //constant variable for intiial range for tax group1
    final double INCOME_RANGE_2 = 18200;    //constant variable for tax group 1 limit
    final double INCOME_RANGE_3 = 37000;    //constant variable for tax group 2 limit
    final double INCOME_RANGE_4 = 87000;    //constant variable for tax group 3 limit
    final double INCOME_RANGE_5 = 180000;   //constant variable for tax group 4 limit
    final double TAX_BRACKET_1_2 = 0;       //initial tax for tax group 1 and 2
    final double TAX_BRACKET_3 = 3572;      //initial tax for tax group 3
    final double TAX_BRACKET_4 = 19822;     //initial tax for tax group 4
    final double TAX_BRACKET_5 = 54097;     //initial tax for tax group 5
    final double TAX_RATE_1 = 0;            //additional tax rate for tax group 1
    final double TAX_RATE_2 = 0.19;         //additional tax rate for tax group 2
    final double TAX_RATE_3 = 0.325;        //additional tax rate for tax group 3
    final double TAX_RATE_4 = 0.37;         //additional tax rate for tax group 4
    final double TAX_RATE_5 = 0.45;         //additional tax rate for tax group 5
    //declaring variable as container per tax group count
    int countGroup_1;
    int countGroup_2;
    int countGroup_3;
    int countGroup_4;
    int countGroup_5;
    
    int maxGroup;                       //initializing variable to store tax group with highest number of employees
                                        //assigning number tax group number
    int taxGroup1 = 1;
    int taxGroup2 = 2;
    int taxGroup3 = 3;
    int taxGroup4 = 4;
    int taxGroup5 = 5;
    int maxTaxGroup;
    
    // constructor initializes instance variable
    public Tax ( double taxableIncome , int group1 , int group2 , int group3 , int group4 , int group5 )
    {
        this.employeeIncome = taxableIncome;
        this.countGroup_1 = group1;
        this.countGroup_2 = group2;
        this.countGroup_3 = group3;
        this.countGroup_4 = group4;
        this.countGroup_5 = group5;
        
    }
    
    //performs tax calculation per tax group once employee salary has been captured
    public double calculateTax(double taxableIncome)
    {
        this.employeeIncome = taxableIncome;
        
        //taxable income $0 - $18200
        if ( employeeIncome >= INCOME_RANGE_1 && employeeIncome <= INCOME_RANGE_2)
        {
            incomeTax = TAX_BRACKET_1_2 + ( TAX_RATE_1 * ( employeeIncome - INCOME_RANGE_1 ) );
        }
        
        //taxable income $18201 - $37000
        else if ( employeeIncome > INCOME_RANGE_2 && employeeIncome <= INCOME_RANGE_3 )
        {
            incomeTax = TAX_BRACKET_1_2 + ( TAX_RATE_2 * ( employeeIncome - INCOME_RANGE_2 ) );
        }
        
        //taxable income $37001 - $87000
        else if ( employeeIncome > INCOME_RANGE_3 && employeeIncome <= INCOME_RANGE_4 )
        {
            incomeTax = TAX_BRACKET_3 + ( TAX_RATE_3 * ( employeeIncome - INCOME_RANGE_3 ) );
        }
        
        //taxable income $87001 - $ 180000
        else if ( employeeIncome > INCOME_RANGE_4 && employeeIncome <= INCOME_RANGE_5 )
        {
            incomeTax = TAX_BRACKET_4 + ( TAX_RATE_4 * ( employeeIncome - INCOME_RANGE_4 ) );
        }   
        
        //taxable income $180001 and over
        else if ( employeeIncome > INCOME_RANGE_5 )
        {
            incomeTax = TAX_BRACKET_5 + ( TAX_RATE_5 * ( employeeIncome - INCOME_RANGE_5 ) );
        }
                
        return incomeTax; //return computed income tax based from the salary input
        
    }
    
    //determine highest tax computed
    public void setHighest()
    {
        if ( incomeTax > highestTax )
        {
            highestTax = incomeTax;
        }
    }
   
   //return highest income tax determined from setHighest()
    public double getHighestTax()
    {
        return highestTax;
    }
    
    //determine lowest tax computed
    public void setLowest ()
    {
       
       if ( lowestTax == i )
       {
           lowestTax = incomeTax;
           --i;
       }
       else if ( incomeTax < lowestTax )
        {
            //set value of lowest tax with current income tax
            lowestTax = incomeTax;
        }
        
    }
    
    //return lowest income tax determined from setLowestTax()
    public double getLowestTax()
    {
        return lowestTax;
    }
   
    //determine group with the highest number of employees
    public int highestTaxGroup( int group1 , int group2 , int group3 , int group4 , int group5)
    {
        //get values counted from main class
        countGroup_1 = group1;
        countGroup_2 = group2;
        countGroup_3 = group3;
        countGroup_4 = group4;
        countGroup_5 = group5;
        
        //compare count per group to determine the highest count
        maxGroup = Math.max(Math.max(Math.max(Math.max(countGroup_1,countGroup_2),countGroup_3),countGroup_4),countGroup_5);
        
        //compares maxGroup count with count per tax group
        if (maxGroup == countGroup_1)
        {
            maxTaxGroup = taxGroup1;    //assigns taxgroup number to maxTaxGroup if condition is met
        }
        if (maxGroup == countGroup_2)
        {
            maxTaxGroup = taxGroup2;
        }
        if (maxGroup == countGroup_3)
        {
            maxTaxGroup = taxGroup3;
        }
        if (maxGroup == countGroup_4)
        {
            maxTaxGroup = taxGroup4;
        }
        if (maxGroup == countGroup_5)
        {
            maxTaxGroup = taxGroup5;
        }
        
        return maxTaxGroup; //return group number of determined highest tax group
         
    }
}
