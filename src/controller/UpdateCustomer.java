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
import model.Customer;
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

/** UpdateCustomer controller class. Handles the UpdateCustomer.fxml view where the user can update an existing customer.
 * Implements the Initializable interface. */
public class UpdateCustomer implements Initializable {
    private Customer customerToModify = ViewCustomers.customerToModify;
    private CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    private FirstLevelDivisionDAOImpl firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();
    private CountryDAOImpl countryDAO = new CountryDAOImpl();
    private FilterDivisions filterDivisions = new FilterDivisions();

    private String name;
    private String phone;
    private String zip;
    private String fullAddress;
    private Country selectedCountry;
    private FirstLevelDivision selectedDivision;

    @FXML
    private ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList();

    @FXML
    private TextField customerIDTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField zipTextField;

    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    /** UpdateCustomer constructor. Initializes the UpdateCustomer class and handles any SQLException errors from database queries.  */
    public UpdateCustomer() throws SQLException {
    }

    /** getFirstLevelDivision. Gathers a firstLevelDivision based on ID from the database.
     *  Throws a SQL Exception if any database errors occur.
     * @param divisionID integer divisionID of the first level division to get.
     * @return returns specific FirstLevelDivision from database. */
    private FirstLevelDivision getFirstLevelDivision(int divisionID) throws SQLException {
        return firstLevelDivisionDAO.getFirstLevelDivision(divisionID);
    }

    /** gatherInformation. Gathers all information from the user including text field and combo boxes. */
    private void gatherInformation() {
        name = nameTextField.getText().trim();
        phone = phoneTextField.getText().trim();
        fullAddress = addressTextField.getText().trim();
        zip = zipTextField.getText().trim();
        selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        selectedDivision = divisionComboBox.getSelectionModel().getSelectedItem();

    }

    /** validationInformation. Validates all information. First checks to see if any text-fields or combo boxes are blank,
     *  displays and alert if any are blank and tells the user to correct it.
     *  Checks to see if length of zip code is over 5 characters and displays an alert telling the user to correct if necessary.
     *  if all information is valid, returns true.
     *  @return boolean. True if all information is valid, otherwise returns false. */
    private boolean validateInformation() {
        gatherInformation();
        if(name.isBlank() || phone.isBlank() || fullAddress.isBlank() || zip.isBlank()
                || selectedCountry == null || selectedDivision == null) {
            Alert missingInfo = new Alert(Alert.AlertType.WARNING);
            missingInfo.setTitle("Missing information");
            missingInfo.setContentText("Please fill out all fields");
            missingInfo.showAndWait();
            return false;
        } else if(zip.length() > 5) {
            Alert zipAlert = new Alert(Alert.AlertType.WARNING);
            zipAlert.setTitle("Zip Code Length");
            zipAlert.setContentText("Zipcode must not be longer than 5 characters");
            zipAlert.showAndWait();
            return false;
        }
        return true;
    }

    /** selectCountry. This method is responsible for filtering the divisions based on what country is selected.
     * For example if the US is selected, only US states are available in the division combo box.
     * @param event handles any kind of action, in this case when a country is selected. */
    @FXML
    void onActionSelectCountry(ActionEvent event) throws SQLException {
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
        if(validateInformation()) {
            try {
                customerDAO.updateCustomer(customerToModify.getCustomerID(), name, fullAddress, zip, phone, selectedDivision.getDivisionID(), LoginController.userName);
                SwitchScene.switchScreen(event, "ViewCustomers.fxml");
            } catch (SQLException | IOException e ) {
                System.out.println("There was an error: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    /** initialize. This implements the Initializable interface. Pre-populates all text-fields and combo boxes from existing
     * customer data.
     * @param url used to resolve relative paths for root object.
     * @param rb localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // populate existing text fields and combo boxes
        customerIDTextField.setText(String.valueOf(customerToModify.getCustomerID()));
        nameTextField.setText(customerToModify.getCustomerName());
        phoneTextField.setText(customerToModify.getPhone());
        addressTextField.setText(customerToModify.getAddress());
        zipTextField.setText(customerToModify.getPostalCode());
        phoneTextField.setText(customerToModify.getPhone());

        countryComboBox.setItems(countryDAO.getAllCountries());
        // check to see division id from customer and select correct country
        if(customerToModify.getDivisionID() >= 1 && customerToModify.getDivisionID() <= 54){
            countryComboBox.getSelectionModel().select(0);
        } else if (customerToModify.getDivisionID() >= 60 && customerToModify.getDivisionID() <=72) {
            countryComboBox.getSelectionModel().selectLast();
        } else {
            countryComboBox.getSelectionModel().select(1);
        }

        // filter division based on country
        Country country = countryComboBox.getSelectionModel().getSelectedItem();
        try {
            filteredDivisions = filterDivisions.FilterDivisions(country.getCountryID());
            divisionComboBox.setItems(filteredDivisions);
            FirstLevelDivision customerOriginalDivision = getFirstLevelDivision(customerToModify.getDivisionID());
            divisionComboBox.getSelectionModel().select(customerOriginalDivision);
        } catch (SQLException e ) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
