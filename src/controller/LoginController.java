package controller;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import utilities.DAO.AppointmentDAOImpl;
import utilities.DAO.UserDAOImpl;
import utilities.GenericAlert;
import utilities.QueryDatabase;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/** LoginController class. Responsible for logging in the user to the application. Handles language translations for either french or english.
 * Implements Initializable interface. */
public class LoginController implements Initializable {
    private Connection conn = Main.conn;
    private String enteredUsername;
    private String enteredPassword;
    private ArrayList<String> dbUserNames = new ArrayList<>();
    private ArrayList<String> dbPasswords = new ArrayList<>();
    private String selectStatement = "SELECT User_Name, Password FROM users";
    private final String filename = "login_activity.txt";
    public static String userName;
    private AppointmentDAOImpl appointmentDAO;
    private UserDAOImpl userDAO;

    @FXML
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    @FXML
    private Label appointmentSchedulerLabel;

    @FXML
    private Label localTimezoneLabel;

    @FXML
    private Label zoneID;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button resetButton;

    @FXML
    private Button loginButton;

    /** checkForAppointments. checks to see if the logged in user has an appointment within 15 minutes, notifies them properly in English or French.
     * @param event responsible for handling the event when the user logs in*/

    private void checkForAppointments(ActionEvent event) {
        try {
            appointmentDAO = new AppointmentDAOImpl();
            userDAO = new UserDAOImpl();
            allAppointments = appointmentDAO.getAllAppointments();
            boolean appointmentFlag  = false;
            int apptID = -1;
            String apptDate ="";
            String apptTime = "";


            // check to see if an appointment occurs within 15 minutes of current time
            LocalTime currentTime = LocalTime.now();
            // search through all appointments in the database to see if an appointment is within 15 minutes
            for(Appointment appointment : allAppointments) {
                LocalTime appointmentStartTime = appointment.getStartDateTime().toLocalTime();
                Long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointmentStartTime);
                if(timeDifference > -1 && timeDifference <= 15) {
                    appointmentFlag = true;
                    apptID = appointment.getAppointmentID();
                    apptDate = appointment.getStartDateFormatted();
                    apptTime = appointment.getStartTimeFormatted();
                    break;
                }

            }
            // If there is an appointment set an alert with the appointment information, otherwise alert says no appointments
            if(appointmentFlag) {

                //Add french alert notifying of upcoming appointment
                try {
                    ResourceBundle resourceBundle = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());
                    // If system is in French
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        GenericAlert blankUserName = new utilities.GenericAlert(resourceBundle.getString("upcoming_appointment_title"),
                                 resourceBundle.getString("upcoming_appointment") + + apptID + "\n" + resourceBundle.getString("date") + apptDate + " " + resourceBundle.getString("time") + apptTime
                                , Alert.AlertType.WARNING);
                        blankUserName.showAlert(event, "MainMenu.fxml");
                    }
                    // If language is not in french show english alert of upcoming appointment
                } catch (MissingResourceException e){
                    StringBuilder appointmentAlertContext = new StringBuilder();
                    appointmentAlertContext.append("You have an upcoming appointment - ID: ").append(apptID).append("\n");
                    appointmentAlertContext.append("Date: ").append(apptDate).append("\n");
                    appointmentAlertContext.append("Time: ").append(apptTime);

                    String title = "Appointment Within 15 Minutes";
                    GenericAlert upcomingAppointments = new GenericAlert(title, appointmentAlertContext.toString(), Alert.AlertType.INFORMATION);
                    upcomingAppointments.showAlert(event, "MainMenu.fxml");
                }

            } else {
                // check if the system language is in French if it is set a no upcoming appointments alert, if not show it in English
                try {
                    ResourceBundle resourceBundle = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        GenericAlert noUpcomingAppointments = new GenericAlert(resourceBundle.getString("no_appointments"),
                                resourceBundle.getString("no_appointments_context"), Alert.AlertType.INFORMATION);
                        noUpcomingAppointments.showAlert(event, "MainMenu.fxml");
                    }
                    // show english alert if system is not in english
                } catch (MissingResourceException e) {
                    String title = "No Upcoming Appointments";
                    String contextText = "You have no upcoming appointments within 15 minutes";
                    GenericAlert noUpcomingAppointments = new GenericAlert(title, contextText, Alert.AlertType.INFORMATION);
                    noUpcomingAppointments.showAlert(event, "MainMenu.fxml");
                }
            }
        // if there is a database error or IO error show the message
        } catch (SQLException | IOException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** logActivity. Accepts a boolean and a user string, depending on if a user successfully logs in or not a message
     * is logged to login_activity.txt.
     * @param logFlag boolean as to whether the login is successful or not.
     * @param user String of the user trying to log in. */
    private void logActivity(boolean logFlag, String user) throws IOException {
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());

        String successfulLogin = "User '" + user + "' successfully logged in on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + localZoneID.toString() + " time";
        String unknownUserLogin = "Unknown User " + user + " failed to log in on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + localZoneID.toString() + " time";
        String failedLogin = "User '" + user + "' failed to log in on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + localZoneID.toString() + " time";
        // if login is successful add it to login_activity.txt file
        if(logFlag) {
            outputFile.println(successfulLogin);
        } else if(user.isBlank()) {
            // if the user is blank add unknown user login_activity.txt
            outputFile.println(unknownUserLogin);
        } else {
            // if the login failed add failed user to login_activity.txt
            outputFile.println(failedLogin);
        }
        outputFile.close();
    }


    /** login. Responsible for authenticating user. Logs them in if username and password is acceptable, doesn't allow log in if information is inaccurate.
     * @return boolean true if the user is accepted, otherwise false. */
    private boolean login() throws SQLException {

        QueryDatabase.setPreparedStatement(conn, selectStatement);

        // grab the list of usernames
        try {
            PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dbUserNames.add(resultSet.getString("User_Name"));
            }
        } catch(SQLException e ) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }

        // grab the list of passwords
        try {
            PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
            QueryDatabase.setPreparedStatement(conn, selectStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                dbPasswords.add(resultSet.getString("Password"));
            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }

        // get the username and password information from the user
        enteredUsername = usernameTextField.getText().toLowerCase().trim();
        enteredPassword = passwordField.getText();

        // if the usernames and passwords match up, accept them. otherwise reject them
        if(enteredUsername.equals(dbUserNames.get(0)) && enteredPassword.equals(dbPasswords.get(0))) {
            return true;

        }
        return false;
    }

    /** blankUserName Method. Tests for blank username. If the system is in French it displays a warning in french
     * if system default is not in French, the warning will be displayed in English.
     * @param  event handles login with blank username. */
    private void blankUserName(ActionEvent event) throws IOException {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());

            // If system default is in French, alerts will appear in French
            if (Locale.getDefault().getLanguage().equals("fr")) {
                GenericAlert blankUserName = new utilities.GenericAlert(resourceBundle.getString("username_required"), resourceBundle.getString("username_context"), Alert.AlertType.WARNING);
                blankUserName.showAlert(event, "Login.fxml");
                //If system default is English
            }
        } catch (MissingResourceException e) {
            GenericAlert blankUserName = new utilities.GenericAlert("Username Required", "Username must not be blank", Alert.AlertType.WARNING);
            blankUserName.showAlert(event, "Login.fxml");
        }
    }
    /** blankPasswordMethod. Tests for blank password. If the system is in French it displays a warning in french
     * if system default is not in French, the warning will be displayed in English.
     * @param  event handles login with a blank password */
    private void blankPassword(ActionEvent event) throws IOException {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());
            // If system is in French
            if (Locale.getDefault().getLanguage().equals("fr")) {
                GenericAlert blankUserName = new utilities.GenericAlert(resourceBundle.getString("password_required"), resourceBundle.getString("password_context"), Alert.AlertType.WARNING);
                blankUserName.showAlert(event, "Login.fxml");
            }
        } catch (MissingResourceException e) {
            GenericAlert blankPassword = new utilities.GenericAlert("Password Required", "Password must not be blank", Alert.AlertType.WARNING);
            blankPassword.showAlert(event, "Login.fxml");
        }
    }

    /** incorrectPasswordUserName Method. Tests for an incorrect password or username. If the system is in French it displays a warning in french
     * if system default is not in French, the warning will be displayed in English.
     * @param  event handles the event when the user clicks login */
    private void incorrectPasswordUserName(ActionEvent event) throws IOException {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                //If system is in French
                GenericAlert blankUserName = new utilities.GenericAlert(resourceBundle.getString("user_password_incorrect"), resourceBundle.getString("user_password_context"), Alert.AlertType.WARNING);
                blankUserName.showAlert(event, "Login.fxml");
            }
        } catch (MissingResourceException e) {
            GenericAlert alertMismatch = new utilities.GenericAlert("Username/Password Incorrect", "Username/Password incorrect", Alert.AlertType.WARNING);
            alertMismatch.showAlert(event, "Login.fxml");
        }
    }

    /** onActionLogin. Authenticates username and password. Logs in user if accepted, and logs activity to login_activity.txt.
     * @param event handles the login action where the user clicks login.*/
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {
        enteredUsername = usernameTextField.getText().toLowerCase().trim();
        enteredPassword = passwordField.getText().trim();
       if(login()) {
           System.out.println("user accepted");
           logActivity(true, enteredUsername);
           userName = enteredUsername;
           checkForAppointments(event);
       } else if(enteredUsername.isBlank()) {
           blankUserName(event);
           logActivity(false, enteredUsername);
       } else if(enteredPassword.isBlank()) {
          blankPassword(event);
          logActivity(false, enteredUsername);
       } else {
          incorrectPasswordUserName(event);
          logActivity(false, enteredUsername);
       }
    }

    /** onActionReset. Lets the reset the password and login fields.
     * @event handles action when the user clicks reset*/
    @FXML
    void onActionReset(ActionEvent event) {
        usernameTextField.clear();
        passwordField.clear();
    }



    /** initialize method implements initializable Interface, initializes LoginController. When scene loads, initialize detects the localization and translates the scene to french or english.
     * The users localZoneID is also displayed to the screen
     * @param url used to resolve relative paths for root object.
     * @param rb localizes the root object*/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set local zoneID of user
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
        zoneID.setText(localZoneID.toString());
        // Translates login screen if system default is French
        try {
            rb = ResourceBundle.getBundle("utilities/Nat", Locale.getDefault());
            //Check to figure out what language the machine is using
            if (Locale.getDefault().getLanguage().equals("fr")) {
                appointmentSchedulerLabel.setText(rb.getString("appointment_scheduler"));
                localTimezoneLabel.setText(rb.getString("local_timezone"));
                usernameTextField.setPromptText(rb.getString("enter_user"));
                passwordField.setPromptText(rb.getString("enter_password"));
                usernameLabel.setText(rb.getString("login"));
                passwordLabel.setText(rb.getString("password"));
                resetButton.setText(rb.getString("reset"));
                loginButton.setText(rb.getString("login"));

                // set userDAOImpl
            }
        } catch(Exception e) {
            // Leave this blank so english warning doesn't convey
        }
    }
}
