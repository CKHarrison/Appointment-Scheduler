package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import utilities.DAO.AppointmentDAOImpl;
import utilities.GeneralErrorMessage;
import utilities.ShowTimezoneID;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** MainMenu class. Responsible for showing the MainMenu.Fxml screen. Shows all the appointments and lets the user
 * switch scenes for customers, add & modify appointments, and view reports. Implements Initializable interface. */
public class MainMenu implements Initializable {

    private AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();



    @FXML
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();


    @FXML
    private ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();

    @FXML
    private  ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();

    @FXML
    private  ObservableList<Appointment> currentMonthAppointments = FXCollections.observableArrayList();

    @FXML
    private Button viewEditCustomerButton;

    @FXML
    private Button scheduleAppointmentButton;

    @FXML
    private Button modifyAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Button reportsButton;

    @FXML
    private RadioButton viewAllRadioButton;

    @FXML
    private ToggleGroup reportViewTgl;

    @FXML
    private RadioButton MonthlyViewRadioButton;

    @FXML
    private RadioButton weeklyViewRadioButton;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> tbAppointmentID;

    @FXML
    private TableColumn<Appointment, String> tbTitle;

    @FXML
    private TableColumn<Appointment, String> tbDescription;

    @FXML
    private TableColumn<Appointment, String> tbLocation;

    @FXML
    private TableColumn<Appointment, String> tbContact;

    @FXML
    private TableColumn<Appointment, String> tbType;

    @FXML
    private TableColumn<Appointment, String> tbDate;

    @FXML
    private TableColumn<Appointment, LocalTime> tbStartTime;

    @FXML
    private TableColumn<Appointment, LocalTime> tbEndTime;

    @FXML
    private TableColumn<Appointment, Integer> tbCustomerID;

    @FXML
    private Button quitButton;

    @FXML
    private Label timezoneID;

    public static Appointment appointmentToModify;

    /** MainMenu constructor. Initializes the MainMenu class instance and grabs all appointments from the database. */
    public MainMenu() throws SQLException {
        // initialize allAppointments observable list to all appointments in the database
        allAppointments = appointmentDAO.getAllAppointments();
    }

    /** viewAllAppointments. Sets up the appointment table view to display all appointments from the database. */
    private void viewAllAppointments() throws SQLException {
        //Setting up table view
        appointmentTableView.setItems(allAppointments);
        tbAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        tbTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tbDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tbContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        tbType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tbDate.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDateFormatted"));
        tbStartTime.setCellValueFactory(new PropertyValueFactory<Appointment, LocalTime>("startTimeFormatted"));
        tbEndTime.setCellValueFactory(new PropertyValueFactory<Appointment, LocalTime>("endTimeFormatted"));
        tbCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentTableView.getSelectionModel().selectFirst();
    }


    /** onActionMonthlyView. Handles the action when the user clicks the monthly view radio button.
     * Only shows appointments for the current month.
     * @param event handles the event when the user clicks the monthly view appointment */
    @FXML
    public void onActionMonthlyView(ActionEvent event) throws IOException {
        // Filter all appointments to only show appointments on the current month
        Month currentMonth = LocalDate.now().getMonth();
        int currentYear = LocalDate.now().getYear();
        System.out.println("Current year: " + currentYear);
        System.out.println(currentMonth);
        //Check to see if monthlyAppointments already has information, if so, clear it to prevent duplicates
        if(!monthlyAppointments.isEmpty()) {
            monthlyAppointments.clear();
        }

        // allAppointments is an observable list of all available appointments, populated on the initialize method
        for(Appointment appointment : allAppointments) {
            if(appointment.getStartDateTime().getMonth().equals(currentMonth) && appointment.getStartDateTime().getYear() ==currentYear ) {
                monthlyAppointments.add(appointment);
            }
        }
//        appointmentTableView.getItems().clear();
        appointmentTableView.setItems(monthlyAppointments);

        // If there are no monthly appointments show alert
        if (monthlyAppointments.isEmpty()) {
            Alert noWeeklyAppointments = new Alert(Alert.AlertType.INFORMATION);
            noWeeklyAppointments.setTitle("No Appointments For This Month");
            noWeeklyAppointments.setContentText("There are no appointments for this month.");
            noWeeklyAppointments.showAndWait();
        }

    }

    /** onActionWeeklyView. Handles the action when the user clicks the weekly view radio button.
     * Only shows the current week's appointments.
     * @param event handles the action when the weekly view radio view button is clicked. */
    @FXML
    public void onActionWeeklyView(ActionEvent event) {
        // get current week of year
        TemporalField currentWeekBasedYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        LocalDate currentDate = LocalDate.now();
        int currentWeekNumber = currentDate.get(currentWeekBasedYear);
        int currentYear = LocalDate.now().getYear();
        System.out.println(currentWeekNumber);

        //Check to see if weeklyAppointments already has information, if so, clear it to prevent duplicates
        if(!weeklyAppointments.isEmpty()) {
            weeklyAppointments.clear();
        }
        // searches for appointments that match the current week and adds them to an overall list
        for (Appointment appointment : allAppointments) {
            TemporalField appointmentWeek = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int appointmentWeekNumber = appointment.getStartDateTime().toLocalDate().get(appointmentWeek);
            if (appointmentWeekNumber == currentWeekNumber && appointment.getStartDateTime().getYear() == currentYear) {
                weeklyAppointments.add(appointment);
            }
        }

        appointmentTableView.setItems(weeklyAppointments);
        // If there are no weekly appointments show alert
        if (weeklyAppointments.isEmpty()) {
            Alert noWeeklyAppointments = new Alert(Alert.AlertType.INFORMATION);
            noWeeklyAppointments.setTitle("No Appointments For This Week");
            noWeeklyAppointments.setContentText("There are no appointments for this week.");
            noWeeklyAppointments.showAndWait();
        }

    }


    /** onActionModifyAppointment. Switches the scene to UpdateAppointment.fxml view.
     * @param event handles the event when the Modify Appointment button is clicked. */
    @FXML
    public void onActionModifyAppointment(ActionEvent event) throws IOException {
        appointmentToModify = appointmentTableView.getSelectionModel().getSelectedItem();
        SwitchScene.switchScreen(event, "UpdateAppointment.fxml");
    }


    /** onActionViewCustomer. Switches the scene to the ViewCustomers Screen.
     * @param event handles the event when the user clicks the view customer button */
    @FXML
    public void onActionViewCustomer(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ViewCustomers.fxml");
    }

    /** onActionDeleteAppointment. Deletes the selected appointment if the user accepts the delete alert warning.
     * @param event handles the event when the user clicks the Delete Appointment button.  */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws SQLException, IOException {
        try {
            // grab the selected appointment and display an alert warning the user about deleting an appointment
            Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
            String appointmentTitle = appointmentToDelete.getTitle();
            Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            deleteConfirmation.setTitle("Delete Confirmation");
            deleteConfirmation.setContentText("Are you sure you want to delete " + appointmentToDelete.getTitle() + "?");
            Optional<ButtonType> result = deleteConfirmation.showAndWait();
            // if the user okay's the delete, delete the appointment from the database and update the table view
            if(result.isPresent() && result.get() == ButtonType.OK) {
                allAppointments.remove(appointmentToDelete);
                appointmentDAO.deleteAppointment(appointmentToDelete.getAppointmentID());
                Alert appointmentCancellation = new Alert(Alert.AlertType.INFORMATION);
                appointmentCancellation.setTitle("Appointment Cancelled");
                String appointmentCancellationContext = "Appointment '" + appointmentTitle + "' was cancelled.";
                appointmentCancellation.setContentText(appointmentCancellationContext);
                allAppointments.remove(appointmentToDelete);
                appointmentCancellation.showAndWait();
            }
            // if the appointment doesn't exist warn the user to select another appointment
        } catch (NullPointerException e) {
            Alert selectAppointment = new Alert(Alert.AlertType.ERROR);
            selectAppointment.setTitle("Select Appointment to Delete");
            selectAppointment.setContentText("Please select an appointment to delete from the table view");
            selectAppointment.showAndWait();
        }
    }


    /** onActionQuit. Handles the action when the user clicks the quit button. Switches the scene to the QuitConfirmation.fxml view.
     * @param event handle the event when the user clicks the quit button */
    @FXML
    public void onActionQuit(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "QuitConfirmation.fxml");
    }

    /** onActionReports. Handle the action when the user clicks on the view reports button. Switches scene to ViewReports.fxml.
     * @param event handles the event when the user clicks the view report button */
    @FXML
    public void onActionReports(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ReportView.fxml");
    }

    /** onActionScheduleAppointment. Handle the action when the user clicks the schedule appointment. Switches scene to ScheduleAppointment.fxml.
     * @param event handle the event when the user clicks the Schedule Appointment button. */
    @FXML
    public void onActionScheduleAppointment(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ScheduleAppointment.fxml");
    }

    /** onActionViewAll. Handle the action where the user clicks the view all radio button. Displays all appointments in the database to the table view.
     * @param event handle the event where the user clicks the view all radio button. */
    @FXML
    public void onActionViewAll(ActionEvent event) {
        try {
            viewAllAppointments();
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /** initialize. Implements Initializable interface. Uses two lambdas, setTimezone to display the users timezone ID.
     * Uses GeneralErrorMessage to print an error to the console if an error occurs while setting up the table view.
     * Sets up table view to display all appointments by calling the viewAllAppointments method. Defaults the view radio button to being selected.
     * @param url used to resolve relative paths for root object.
     * @param rb localizes the root object*/
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        viewAllRadioButton.setSelected(true);

        //Timezone lambda to set up the timezone for display on the main menu when initializing.
        ShowTimezoneID setTimezone = () -> {
          timezoneID.setText(ZoneId.of(TimeZone.getDefault().getID()).toString());
        };
        setTimezone.displayTimeZoneID();

        // Lambda message interface to let the user know there was an error with setting up the table view if an error occurs
        GeneralErrorMessage message = e ->{
            System.out.println("There was an error while setting up the table view");
        } ;

        // Call viewAllAppointments to set up the initial view to display all appointments
        try {
            viewAllAppointments();
        } catch (SQLException e) {
            message.errorMessage(e);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
