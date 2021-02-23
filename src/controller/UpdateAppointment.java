package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import utilities.DAO.AppointmentDAOImpl;
import utilities.DAO.ContactDAOImpl;
import utilities.DAO.CustomerDAOImpl;
import utilities.DAO.UserDAOImpl;
import utilities.GenericAlert;
import utilities.SwitchScene;
import utilities.TimeConversion;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/** UpdateAppointment controller class. Responsible for populating the UpdateAppointment.FXML view. Implements Initializable interface. */
public class UpdateAppointment implements Initializable {

    @FXML
    private TextField appointmentIDTextField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private DatePicker datePickerBox;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private ComboBox<User> userComboBox;


    @FXML
    private ComboBox<LocalTime> startTimeComboBox;


    @FXML
    private ComboBox<LocalTime> endTimeComboBox;


    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;


    private LocalTime start = LocalTime.of(4,0);
    private LocalTime end = LocalTime.of(23, 0);
    private ContactDAOImpl contactDAO = new ContactDAOImpl();
    private UserDAOImpl userDAO = new UserDAOImpl();
    private CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    private AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    private ObservableList<String> types = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private Customer customer;
    private User user;
    private String title;
    private String description;
    private String location;
    private Contact contact;
    private String type;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Appointment appointmentToModify = MainMenu.appointmentToModify;


    /** UpdateAppointment constructor. Initializes UpdateAppointment class, and throws exception if there is a database query error */
    public UpdateAppointment() throws SQLException {
    }


    /** gatherInformation. Gathers all of the information from the user including text-fields, datepicker, and combo boxes. */
    private void gatherInformation() {
        customer = customerComboBox.getSelectionModel().getSelectedItem();
        user = userComboBox.getSelectionModel().getSelectedItem();
        title = titleTextField.getText().trim();
        description = descriptionTextField.getText().trim();
        location = locationTextField.getText().trim();
        contact = contactComboBox.getSelectionModel().getSelectedItem();
        type = typeComboBox.getSelectionModel().getSelectedItem();
        date = datePickerBox.getValue();
        startTime = startTimeComboBox.getValue();
        endTime = endTimeComboBox.getValue();
    }

    /** validateTimes. Validates the user's entered times and compares them with Eastern Time business hours.
     * @return true if times are within business hours, false if not. */
    private boolean validateTimes() {
        LocalDateTime potentialStartDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime potentialEndDateTime = LocalDateTime.of(date, endTime);
        return TimeConversion.compareWithBusinessHours(potentialStartDateTime) && TimeConversion.compareWithBusinessHours(potentialEndDateTime);
    }

    /** overlappingTimes. Checks to see if times overlap with other appointments for the same customer. Will not compare
     * hours to itself.
     * @return true if times do not overlap, false if they do. */
    private boolean overlappingTimes() {
        try {
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            appointments = appointmentDAO.getAllAppointments();
            for(Appointment appointment : appointments) {
                if(appointment.getAppointmentID() != appointmentToModify.getAppointmentID()) {
                    if(appointment.getCustomerID() == customer.getCustomerID()) {
                        // check to see if start time is between other meeting's start and end time
                        if(startDateTime.isAfter(appointment.getStartDateTime().minusMinutes(1)) && startDateTime.isBefore(appointment.getEndDateTime())) {
                            Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                            overlapAlert.setTitle("Overlapping appointments");
                            overlapAlert.setContentText("Your appointment overlaps with " + appointment.getTitle());
                            overlapAlert.showAndWait();
                            return false;
                        } else if (endDateTime.isAfter(appointment.getStartDateTime().minusMinutes(1)) && endDateTime.isBefore(appointment.getEndDateTime())) {
                            Alert overlapAlert = new Alert(Alert.AlertType.ERROR);
                            overlapAlert.setTitle("Overlapping appointments");
                            overlapAlert.setContentText("Your appointment overlaps with " + appointment.getTitle());
                            overlapAlert.showAndWait();
                            return false;
                        }
                    }
                }

            }

        } catch (SQLException e) {
            System.out.println("There was an error: " +e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /** validateInformation. Gathers all information and runs through validations. If any fields are blank, displays an
     * alert telling the user to correct it. If start time is after end time, displays alert and tells user to correct it.
     * If times are not valid, it will display a warning to the user for correction. If all validations pass sets the start and end time.
     * @return true if all validations pass, otherwise returns false. */
    private boolean validateInformation() {
        gatherInformation();
        System.out.println("Are these times valid? " + validateTimes());
        if (customer == null || user == null || title.isBlank() || description.isBlank() || location.isBlank() ||
                contact == null || type == null || date == null || startTime == null || endTime == null ) {
            Alert missingInformation = new Alert(Alert.AlertType.ERROR);
            missingInformation.setTitle("Missing Information");
            missingInformation.setContentText("Please fill out all fields");
            missingInformation.showAndWait();
            return false;
        } else if(startTime.isAfter(endTime) || startTime.equals(endTime)){
            Alert startEndConflict = new Alert(Alert.AlertType.ERROR);
            startEndConflict.setTitle("Appointment time conflict.");
            startEndConflict.setContentText("Appointment starting time must be before ending time.");
            startEndConflict.showAndWait();
            return false;
        } else if(!validateTimes()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Appointment Hours must be between 8:00 AM and 10:00PM Eastern Time\n");
            sb.append("Your start time in eastern time is ").append(TimeConversion.formatTime(TimeConversion.convertLocalToEastern(LocalDateTime.of(date, startTime))));
            sb.append("\nYour start time in eastern time is ").append(TimeConversion.formatTime(TimeConversion.convertLocalToEastern(LocalDateTime.of(date, endTime))));
            sb.append("\nYour current time is ").append(TimeConversion.formatTime(LocalTime.now()));
            sb.append("\nEastern time is ").append(TimeConversion.formatTime(TimeConversion.convertLocalToEastern(LocalDateTime.now())));
            Alert invalidHours = new Alert(Alert.AlertType.ERROR);
            invalidHours.setTitle("Invalid Appointment Hours");
            invalidHours.setContentText(sb.toString());
            invalidHours.showAndWait();
            return false;
        }  else if(!overlappingTimes()) {
            return false;
        } else {
            // if validations pass set start and end time
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            return true;
        }
    }

    /** onActionCancel. Handles the action where the user wants to cancel. Displays alert and switches scene back to main menu
     * if the user clicks ok.
     * @param event Handles the event where the user clicks the cancel button. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        GenericAlert cancelAlert = new GenericAlert("Cancel", "Are you sure you want to cancel?", Alert.AlertType.CONFIRMATION);
        cancelAlert.showAlert(event, "MainMenu.fxml");
    }

    /** onActionSave. Handles the action where the user wants to save the changes to the appointment. If information is valid
     * appointment updates are saved.
     * @param event handles event where the user clicks the save button. */
    @FXML
    void onActionSave(ActionEvent event) throws SQLException, IOException {
        if (validateInformation()) {
            appointmentDAO.updateAppointment(appointmentToModify.getAppointmentID(), title, description, location, type, startDateTime, endDateTime, customer.getCustomerID(), user.getUserID(),
                    contact.getContactID());
            SwitchScene.switchScreen(event, "MainMenu.fxml");
        }
    }

    /** initialize. Implements Initializable interface. Handles the set up of UpdateAppointment controller. Prepopulates all
     * fields with existing appointment information.
     * @param url used to resolve relative paths for root object.
     * @param resourceBundle localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set id
        appointmentIDTextField.setText(String.valueOf(appointmentToModify.getAppointmentID()));
        // Set appointment types
        types.addAll("All Staff", "Department Meeting", "Planning Session", "De-Briefing", "Scrum Meeting");
        typeComboBox.setItems(types);
        int appointmentToModifyTypeIndex = types.indexOf( appointmentToModify.getType());
        typeComboBox.getSelectionModel().select(appointmentToModifyTypeIndex);


        try {
            // populate customer combo box and select current customer
            customerComboBox.setItems(customerDAO.getAllCustomers());
            customerComboBox.getSelectionModel().select(customerDAO.getCustomer(appointmentToModify.getCustomerID()));

            //  populate user combo box and select current user
            userComboBox.setItems(userDAO.getAllUsers());
            userComboBox.getSelectionModel().select(userDAO.getUser(appointmentToModify.getUserID()));
            // populate contact combo box
            contactComboBox.setItems(contactDAO.getAllContacts());
            contactComboBox.getSelectionModel().select(contactDAO.getContact(appointmentToModify.getContactID()));
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        // set title, description, and location
        titleTextField.setText(appointmentToModify.getTitle());
        descriptionTextField.setText(appointmentToModify.getDescription());
        locationTextField.setText(appointmentToModify.getLocation());

        // set datepicker and get initial date value
        datePickerBox.setValue(appointmentToModify.getStartDateTime().toLocalDate());
        date = datePickerBox.getValue();

        //populate times and select time
        TimeConversion.populateTimes(startTimeComboBox, start, end);
        TimeConversion.populateTimes(endTimeComboBox, start, end);
        startTimeComboBox.getSelectionModel().select(appointmentToModify.getStartDateTime().toLocalTime());
        endTimeComboBox.getSelectionModel().select(appointmentToModify.getEndDateTime().toLocalTime());


    }
}
