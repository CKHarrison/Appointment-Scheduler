package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** ScheduleAppointment class. Responsible for adding a schedule to the database. Implements the Initializeable interface */
public class ScheduleAppointment implements Initializable {

    @FXML
    private TextField AppointmentIDTextField;

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

    /** ScheduleAppointment constructor. Responsible for initialize ScheduleAppointment class. throws SQLException if database queries error. */
    public ScheduleAppointment() throws SQLException {
    }


    /** gatherInformation. Gathers all user input from text fields and combo boxes. */
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

    /** validateTimes. Validates the user's local start and end time, and compares them to Eastern Standard Time business hours.
     * @return boolean true if the start and end times are valid, otherwise returns false. */
    private boolean validateTimes() {
        LocalDateTime potentialStartDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime potentialEndDateTime = LocalDateTime.of(date, endTime);
        return TimeConversion.compareWithBusinessHours(potentialStartDateTime) && TimeConversion.compareWithBusinessHours(potentialEndDateTime);
    }

    /** validateInformation. Validates all the users input information. Gathers all information. If the fields are blank or null
     * displays and alert warning the user to correct them. If the start time is after then end time displays an alert telling the user to correct them.
     * if the times are invalid when compared to business hours, displays alert for the user to correct them. Returns true
     * and sets start and end times if all validations are passed.
     * @return boolean. False if validations are not passed. True otherwise. */
    private boolean validateInformation() {
        gatherInformation();
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
            sb.append("\nYour end time in eastern time is ").append(TimeConversion.formatTime(TimeConversion.convertLocalToEastern(LocalDateTime.of(date, endTime))));
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
            // set start time and end time, and return true
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            return true;
        }
    }

    /** overlappingTimes. Check to see if user entered times overlap with any existing appointments with the same customer.
     *  Will display warning to user if times overlap, and will show appointment title that they overlap with.
     * @return boolean. False if times overlap. Otherwise returns true. */
    private boolean overlappingTimes() {
        try {
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
            appointments = appointmentDAO.getAllAppointments();
            for(Appointment appointment : appointments) {
                if(appointment.getCustomerID() == customer.getCustomerID()) {
                    // check to see if start time is between other meeting's start and end time and vice versa
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
        } catch (SQLException e) {
            System.out.println("There was an error: " +e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /** onActionCancel. Handles the action where the user wants to cancel the scheduling of an appointment, and switches the
     * scene back to the MainMenu.FXML view.
     * @param event handles the event where the user clicks the cancel button. */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        GenericAlert cancelAlert = new GenericAlert("Cancel", "Are you sure you want to cancel?", Alert.AlertType.CONFIRMATION);
        cancelAlert.showAlert(event, "MainMenu.fxml");
    }

    /** onActionSave. Handles the action where the user wants to save the appointment. If it is valid, it will save the appointment to the
     * database and then switch the user back to the MainMenu.fxml scene.
     * @param event handles the event where the user clicks the save button. */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException, SQLException {
        if (validateInformation()) {
            appointmentDAO.addAppointment(title, description, location, type, startDateTime, endDateTime, LocalDateTime.now(),
                    LoginController.userName, LocalDateTime.now(), LoginController.userName, customer.getCustomerID(), user.getUserID(),
                    contact.getContactID());
            SwitchScene.switchScreen(event, "MainMenu.fxml");
        }
    }

    /** initialize. Implements the Initializable interface, sets up the ScheduleAppointment.fxml view and populates the
     * types, user, contacts, and date picker fields.
     * @param url used to resolve relative paths for root object.
     * @param rb localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Add types to combo box
        types.addAll("All Staff", "Department Meeting", "Planning Session", "De-Briefing", "Scrum Meeting");
        typeComboBox.setItems(types);
        titleTextField.requestFocus();

        // set up contact and user combo boxes
        contactComboBox.setItems(contactDAO.getAllContacts());
        userComboBox.setItems(userDAO.getAllUsers());
        try {
            customerComboBox.setItems(customerDAO.getAllCustomers());
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        // Format DatePicker and enable manually entered dates
        datePickerBox.setConverter(new StringConverter<LocalDate>() {
            String format = "MM-dd-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            {
                datePickerBox.setPromptText("Pick a date from the picker ");
            }
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null) {
                    return dateFormatter.format(localDate);
                } else {
                    return " ";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        // set the date picker to current date automatically and grab the value
        datePickerBox.setValue(LocalDate.now());
        date = datePickerBox.getValue();

        // populate the start and end time combo boxes
        TimeConversion.populateTimes(startTimeComboBox, start, end);
        TimeConversion.populateTimes(endTimeComboBox, start, end);
        startTimeComboBox.getSelectionModel().selectFirst();
        endTimeComboBox.getSelectionModel().select(1);
    }
}
