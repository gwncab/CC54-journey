import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * File name: HomelessInfoController.java
 * @author gwen cabangon 12119839
 * @author mia megan gail macasero 12127091
 * @lecturer: ishan senarathna
 * compiler java SE 11
 * JavaFX 11
 * This file provides the standard driver class for GUI based applications.
 * This file is referenced from ModelGUI sample code and Assessment 1 solution
 * in COIT20256 Moodle by Mary Tom.
 */
public class HomelessInfoController 
{
    //instance variables for GUI
    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private TabPane hisTabPane;
    //location tab
    @FXML private TextField sa3CodeField;
    @FXML private TextField locationNameField;
    @FXML private Label sa3CodeInvalidErr;
    @FXML private Label locNameInvalidErr;
    @FXML private Label addLocSuccess;
    @FXML private Label saveLocToDbLabel;
    //tenant tab
    @FXML private ChoiceBox<AGE> ageSelect;
    @FXML private ComboBox<WEEKLY_INCOME> incomeSelect;
    @FXML private ChoiceBox<String> incomeSourceSelect;
    @FXML private ChoiceBox<GENDER> genderSelect;
    @FXML private ComboBox<Integer> sa3CodeSelect;
    @FXML private AnchorPane tenantPane;
    @FXML private Label locationNameDisplay;
    @FXML private Label saveTenantToDbLabel;
    @FXML private Label addTenantLabel;
    //report tab
    @FXML private ComboBox<String> reportTypeSelect;
    @FXML private TextArea reportArea;
    @FXML private ComboBox<Integer> sa3CodeReportSelect;
    @FXML private Label codeErr;
    @FXML private Button btnExit;
    
    //instance variable for datafile
    private DataFile data = new DataFile();
    //instance variable for type location
    private Location loc = new Location();
    private Location locationSelected = new Location();
    //instance variable for location arraylist
    private LinkedList<Location> locationList = new LinkedList<>();
    private LinkedList<Integer> codeList;
    //observable list for sa3Code
    private ObservableList<Integer> sa3CodeList = FXCollections.observableArrayList();
    //private final ObservableList<String> incomeRange = FXCollections.observableArrayList(WEEKLY_INCOME.NilIncome.getIncome(), WEEKLY_INCOME.below$1000.getIncome());
    private final ObservableList<String> incomeSource = FXCollections.observableArrayList("Employed","Other");
    //instance variable for type RiskyPersons
    private RiskyPersons p;
    //instance variable for RiskyPersons arraylist
    private LinkedList<RiskyPersons> riskyPersonsList = new LinkedList<>();
    private DatabaseUtility db = new DatabaseUtility();

    //after clicking next button in location tab
    @FXML
    void btnLocNextButtonClicked(ActionEvent event)
    {
        sa3CodeInvalidErr.setText("");
        locNameInvalidErr.setText("");
        saveLocToDbLabel.setText("");
        sa3CodeField.clear();
        locationNameField.clear();
        this.hisTabPane.getSelectionModel().selectNext();
    }
    
    //adding location to locations list
    @FXML
    void btnLocationButtonClicked(ActionEvent event)
    {
        sa3CodeInvalidErr.setText("");
        locNameInvalidErr.setText("");
        addLocSuccess.setText("");
        
        int sa3Code = 0;
        String locname = "";
        boolean error = false, codeExist = false, nameExist = false;
        //validate sa3Code input

        try{
            if(!sa3CodeField.getText().isEmpty()){
                sa3Code = Integer.parseInt(sa3CodeField.getText());
                loc.setCode(sa3Code);
            }
            else sa3CodeInvalidErr.setText("Code is empty!");
        }
        catch(InputMismatchException e){
             sa3CodeInvalidErr.setText(e.getMessage());
             error =true;
        }
        catch(NumberFormatException e){
            sa3CodeInvalidErr.setText("Enter 5-digit SA3Code");
        }
        
        //validate location name input
        try{
            if(!locationNameField.getText().isEmpty()){
                locname = locationNameField.getText();
                loc.setName(locname);
            }
            else locNameInvalidErr.setText("Name is empty!");
        }
        catch(InputMismatchException e){
            locNameInvalidErr.setText(e.getMessage());
            error = true;
        }

        //validate if location code and name exists
        if(sa3CodeInvalidErr.getText().isEmpty() && locNameInvalidErr.getText().isEmpty()){//if(!error)
            for(Location l : locationList){
                if(db.getCurrentLocationDb().contains(sa3Code)){
                    codeExist = true;
                    sa3CodeInvalidErr.setText("Code already exist!");
                }
                else if(l.getCode() == sa3Code){
                    sa3CodeInvalidErr.setText("Location name already exist!");
                    codeExist = true;
                }
                else{
                    codeExist = false;
                }
                
                if(!l.getName().equalsIgnoreCase(locname)){
                        nameExist = false;
                }
                else {
                    locNameInvalidErr.setText("Location name already exist.");
                    nameExist = true;
                }
            }
            
            
            if(!codeExist && !nameExist){
                //set new list
                locationList.add(new Location(sa3Code, locname));
                loc.setLocationList(locationList);
                //update sa3code list
                addLocSuccess.setText(sa3Code + " added!");
                
            }
            else addLocSuccess.setText("");
        }
        
        //clear sa3Code and locationName fields
        sa3CodeField.clear();
        locationNameField.clear();
        
    }
    
    //adding location record in list to database
    @FXML
    void btnLocationSaveToDbButtonClicked(ActionEvent event){
        //clear sa3Code and locationName fields
        sa3CodeField.clear();
        locationNameField.clear();
        sa3CodeInvalidErr.setText("");
        locNameInvalidErr.setText("");
        
        if(addLocSuccess.getText().isEmpty())
           saveLocToDbLabel.setText("Unsucessful!");
        else {
            if(db.insertLocation(locationList)){
                saveLocToDbLabel.setText("Saved!");
                codeList = db.getCurrentLocationDb();
                updateCodeList();
            }
            else saveLocToDbLabel.setText("Unsucessful!");
        }
        
        addLocSuccess.setText("");
    }
    
    //adding tenant to riskypersons list
    @FXML
    void btnTenantAddButtonClicked(ActionEvent event)
    {
        saveTenantToDbLabel.setText("");
        locationNameDisplay.setText("");
        int count = 1;
        int sa3CodeInput = 0;
        int sa3Code = 0;
        String locName = "";
        AGE ageInput;
        GENDER genderInput;
        WEEKLY_INCOME incomeInput;
        String incomeSourceInput = "";
        
        System.out.println(ageSelect.getValue());
        System.out.println(genderSelect.getValue());
        System.out.println(incomeSelect.getValue());
        System.out.println(incomeSourceSelect.getValue());
        System.out.println(sa3CodeSelect.getValue());
        
        try{
            sa3CodeInput = sa3CodeSelect.getValue();
            
            for(Location locn : db.getLocationListDb()){
                if(locn.getCode() == sa3CodeInput){
                    sa3Code = locn.getCode();
                    locName = locn.getName();
                    locationSelected = new Location(sa3Code, locName);
                }
            }
            ageInput = ageSelect.getValue();
            genderInput = genderSelect.getValue();
            incomeInput = incomeSelect.getValue();
            incomeSourceInput = incomeSourceSelect.getValue();
            SA3TenantCategory tmp = new SA3TenantCategory(ageInput, genderInput,
                    new Location(sa3Code, locName), incomeInput, incomeSourceInput);
            p = new RiskyPersons(tmp, count);
            
            riskyPersonsList.add(p);
            addTenantLabel.setText("Tenant added.");
        }
        catch(NullPointerException e){
            addTenantLabel.setText("Invalid input!");
        }
        
        //clear choicebox and combo boxes after submit
        ageSelect.setValue(null);
        genderSelect.setValue(GENDER.DEFAULT);
        incomeSelect.setValue(null);
        //incomeSelect.setValue(null);
        incomeSourceSelect.setValue(null);
        sa3CodeSelect.setValue(null);
    }
    
    @FXML 
    void sa3CodeSelectSelected(ActionEvent event){
        int sa3CodeInput = sa3CodeSelect.getValue();
        String locName = "";
        for(Location locn : db.getLocationListDb()){
            if(locn.getCode() == sa3CodeInput){
                locName = locn.getName();
            }
        }
        locationNameDisplay.setText(locName);
    }
    
    //adding location record in list to database
    @FXML
    void btnTenantSaveToDbButtonClicked(ActionEvent event){
        System.out.println("Save tenant to db clicked");
        //db.getSA3CategoryList();
        if (addTenantLabel.getText().equals("Tenant added.")){
            if(db.insertRiskyPersons(riskyPersonsList))
                saveTenantToDbLabel.setText("Saved to DB!");
        }
        else saveTenantToDbLabel.setText("Unsuccessful!");
        addTenantLabel.setText("");
    }
    
    
    //after clicking next button in tenant tab
    @FXML
    void btnTenantNextButtonclicked(ActionEvent event){
         saveTenantToDbLabel.setText("");
         addTenantLabel.setText("");
        this.hisTabPane.getSelectionModel().selectNext();
    }
    
    @FXML
    void reportTypeSelectSelected(ActionEvent Event){
        reportArea.clear();
        String reportSelect =  reportTypeSelect.getValue();
        if(reportSelect.equals("Choose Location")){
                reportArea.appendText("Choose Location.");
                sa3CodeReportSelect.setItems(sa3CodeList);
            }
        else
            sa3CodeReportSelect.setItems(null);
    }
    
    @FXML
    void sa3CodeReportSelectSelected(ActionEvent Event){
        reportArea.clear();
        codeErr.setText("");
    }
    
    //selecting report type and displaying reports
    @FXML
    void btnDisplayButtonClicked(ActionEvent event){
        boolean reportTypeSelected = reportTypeSelect.getSelectionModel().isEmpty();
        boolean sa3CodeSelected = sa3CodeReportSelect.getSelectionModel().isEmpty();
        riskyPersonsList = db.getRiskyPersonsList();
        
        if(!reportTypeSelected){
            reportArea.clear();
            //get value of combo box selection
            String reportSelect =  reportTypeSelect.getValue();
            switch(reportSelect){
                case "All Male":
                    Predicate<RiskyPersons> genderMale = e -> (e.getSA3Category().getGender() == GENDER.MALE);
                    reportArea.appendText(riskyPersonsList.stream().filter(genderMale).sorted(Comparator.comparing(RiskyPersons::getPersonCount).reversed()).collect(Collectors.toList()).toString());
                    break;
                case "All Female":
                    Predicate<RiskyPersons> genderFemale = e -> (e.getSA3Category().getGender() == GENDER.FEMALE);
                    reportArea.appendText(riskyPersonsList.stream().filter(genderFemale).sorted(Comparator.comparing(RiskyPersons::getPersonCount).reversed()).collect(Collectors.toList()).toString());
                    break;
                case "All":
                    for(RiskyPersons risky : riskyPersonsList){
                        reportArea.appendText(risky.toString());
                    }
                    break;
                case "Choose Location":
                    if(!sa3CodeSelected){
                        int codeSelected = sa3CodeReportSelect.getValue();
                        
                        for(RiskyPersons risky : riskyPersonsList){
                            if(risky.getSA3Category().getLocation().getCode() == codeSelected)
                                reportArea.appendText(risky.toString());
                        }
                    }
                    else codeErr.setText("Choose SA3Code!");
                    
                    break;
            }
            
        }
        else{
            reportArea.appendText("Choose report type.");
        }
        
        /*String report ="";
        for(RiskyPersons risky : riskyPersonsList){
            reportArea.appendText(risky.toString());
        } */
    }
    
    @FXML
    void btnExitButtonClicked(ActionEvent event){
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void initialize() 
    {   
        //add item to age selection
        ageSelect.getItems().setAll(AGE.values());
        
        //add item to income selection
        incomeSelect.getItems().setAll(WEEKLY_INCOME.values());
        
        //add items to and initialize the income source
        incomeSourceSelect.setItems(incomeSource);
        
        //add items to and initialize the genderSelect comboBox
        genderSelect.getItems().setAll(GENDER.values());
        genderSelect.setValue(GENDER.DEFAULT); //initial selection is "--"
        
        //initialise location list and codelist
        data.fileRead();
        locationList = data.passLocationList();
        //get risky persons list from data file
        riskyPersonsList = data.passRiskyPersonsList();
        
        //add items to report selection
        reportTypeSelect.getItems().addAll("All Male", "All Female", "All", "Choose Location");
        
        //create db and table
        db.createDBtables();
        //save read data to db
        
        db.insertAge(riskyPersonsList);
        db.insertWeeklyIncome(riskyPersonsList);
        db.insertCSVLocation(locationList);
        db.insertCSVRiskyPersons(riskyPersonsList);
        codeList = db.getCurrentLocationDb();
        updateCodeList();
        
    }
    
    void updateCodeList()
    {
        sa3CodeList = FXCollections.observableArrayList(codeList);
        sa3CodeSelect.setItems(sa3CodeList);
    }
    

}
