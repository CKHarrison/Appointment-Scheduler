package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Country;
import model.FirstLevelDivision;
import utilities.DAO.CountryDAOImpl;
import utilities.DAO.CustomerDAOImpl;
import utilities.DAO.FirstLevelDivisionDAOImpl;
import utilities.FilterDivisions;
import utilities.GenericAlert;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** AddCustomer class. This class controls the AddCustomer screen and is responsible for adding a customer to the database. */
public class AddCustomer implements Initializable {

    private FirstLevelDivisionDAOImpl firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();
    private CountryDAOImpl countryDAO = new CountryDAOImpl();
    private CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    private FilterDivisions filterDivisions = new FilterDivisions();

    private String name;
    private String phone;
    private String street;
    private String city;
    private String zip;
    private String state;
    private String fullAddress;
    private Country selectedCountry;
    private FirstLevelDivision selectedDivision;


    @FXML
    private TextField customerIDTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField zipTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;



    @FXML
    private ObservableList<FirstLevelDivision> firstLevelDivisions = firstLevelDivisionDAO.getAllDivisions();

    @FXML
    private ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Country> countries = countryDAO.getAllCountries();



    /** AddCustomer Constructor Method. Responsible for the initial construction of this method. Throws a SQL Exception in case any database connection method fails. */
    public AddCustomer() throws SQLException {
    }

    /** gatherInformation. This method gathers all the information from the text fields and combo boxes that the user fills out. */
    private void gatherInformation() {
        name = nameTextField.getText().trim();
        phone = phoneTextField.getText().trim();
        street = streetTextField.getText().trim();
        city = cityTextField.getText().trim();
        zip = zipTextField.getText().trim();
        state = stateTextField.getText().toUpperCase().trim();
        selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        selectedDivision = divisionComboBox.getSelectionModel().getSelectedItem();

    }

    /** validateInformation. After calling the gatherInformation method, this method validates all the information.
     * Checks to see if any fields or blank or if the zipcode is longer than 5 characters.
     * @param event handles any kind of action, in this case none are recorded.
     * @return boolean */
    private boolean validateInformation(ActionEvent event) {
       gatherInformation();
       // if any of the fields are blank or not selected throw an alert warning about missing fields
       if(name.isBlank() || phone.isBlank() || street.isBlank() || city.isBlank() || zip.isBlank() || state.isBlank()
       || selectedCountry == null || selectedDivision == null) {
           Alert missingInfo = new Alert(Alert.AlertType.WARNING);
           missingInfo.setTitle("Missing information");
           missingInfo.setContentText("Please fill out all fields");
           missingInfo.showAndWait();
           return false;
           // if the zip code is more than 5 digits throw an alert saying it must be less than 5 digits returnfase
       } else if(zip.length() > 5) {
           Alert zipAlert = new Alert(Alert.AlertType.WARNING);
           zipAlert.setTitle("Zip Code Length");
           zipAlert.setContentText("Zipcode must not be longer than 5 characters");
           zipAlert.showAndWait();
           return false;
       }
       fullAddress = street + " " + city + ", " + state;
       return true;
    }

    /** selectCountry. This method is responsible for filtering the divisions based on what country is selected.
     * For example if the US is selected, only US states are available in the division combo box.
     * @param event handles any kind of action, in this case when a country is selected. */
    @FXML
    void selectCountry(ActionEvent event) throws SQLException {
        // grab the country selected and filter and populate divisions based on the country selected
        Country country = countryComboBox.getSelectionModel().getSelectedItem();
        filteredDivisions = filterDivisions.FilterDivisions(country.getCountryID());
        divisionComboBox.setItems(filteredDivisions);
        divisionComboBox.getSelectionModel().selectFirst();
    }


    /** onActionCancel. This method is responsible for handling when the cancel button is clicked.
     * When clicked an alert lets the user either cancel and return to the ViewCustomers screen or stay on the current screen.
     * @param event handles when the cancel button is clicked. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        GenericAlert cancelAlert = new GenericAlert("Cancel", "Are you sure you want to cancel?", Alert.AlertType.CONFIRMATION);
        cancelAlert.showAlert(event, "ViewCustomers.fxml");
    }


    /** onActionSave(). This method is responsible for what happens when the save button is clicked.
     * validateInformation() is called, and if valid, saves the customer to the database and switches back to ViewCustomers screen.
     * @param event handles the action when the save button is clicked.*/
    @FXML
    void onActionSave(ActionEvent event) {
        // if information is validated, add customer to database and switch to ViewCustomers screen.
        if(validateInformation(event)) {
            try {
                customerDAO.addCustomer(name, fullAddress, zip, phone, LoginController.userName, LoginController.userName, selectedDivision.getDivisionID());
                SwitchScene.switchScreen(event, "ViewCustomers.fxml");
            } catch (SQLException | IOException e ) {
                System.out.println("There was an error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /** initialize. This implements the Initializable interface. Responsible for setting up and populating the country
     * combo box and division combo box.
     * @param url used to resolve relative paths for root object.
     * @param rb localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set up the country and division combo box
        countryComboBox.setPromptText("Pick a Country");
        countryComboBox.setItems(countries);
        divisionComboBox.setPromptText("Select Country first");
        divisionComboBox.setItems(null);

    }
}