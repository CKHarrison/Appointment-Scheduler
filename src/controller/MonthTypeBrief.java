package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import utilities.AppointmentStats;
import utilities.DAO.AppointmentDAOImpl;
import utilities.MonthTypeQuery;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** MonthTypeBrief class. Responsible for displaying the MonthTypeBrief.fxml view. Implements Intializable interface. */
public class MonthTypeBrief implements Initializable {

    private AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    private MonthTypeQuery monthTypeQuery = new MonthTypeQuery();

    @FXML
    private ObservableList<AppointmentStats> allStaffAppointments;

    @FXML
    private ObservableList<AppointmentStats> departmentAppointments;

    @FXML
    private ObservableList<AppointmentStats> planningAppointments;

    @FXML
    private ObservableList<AppointmentStats> debriefingAppointments;

    @FXML
    private ObservableList<AppointmentStats> scrumAppointments;

    @FXML
    private ObservableList<AppointmentStats> information = FXCollections.observableArrayList();

    @FXML
    private ObservableList<Appointment>  allAppointmentInformation = FXCollections.observableArrayList();


    @FXML
    private TableView<AppointmentStats> reportTableView;

    @FXML
    private TableColumn<String, String> monthCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<Appointment, Integer> countCol;

    @FXML
    private Button backButton;

    /** MonthTypeBrief constructor. Sets up class instance and throws SQLException if database issue. */
    public MonthTypeBrief() throws SQLException {

    }

    /** addToList. Uses lambda forEach expression to add individual appointments to a comprehensive observable list that
     *  will display all appointment information. forEach lambda compactly and efficiently allows for adding of appointments to another list.
     *  @param listToAdd  ObservableList<AppointmentStats> list that contains appointments. */
    private void addToList(ObservableList<AppointmentStats> listToAdd) {
        // add each appointment to overall list
        listToAdd.stream().forEach(appointmentStats -> information.add(appointmentStats));
    }

    /** onActionBack. Handles the event where the user clicks the back button to switch to the ReportView.fxml scene.
     * @param event Handles the event where the user clicks the back button */
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ReportView.fxml");
    }

    /** initialize. Implements initializable Interface, initializes MonthTypeView controller. Responsible for grabbing all appointment types and counts from the database.
     * Adds all information to a comprehensive list, and displays all information to the table view.
     * @param url used to resolve relative paths for root object.
     * @param resourceBundle localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // grab all appointment type information from database
        allStaffAppointments = monthTypeQuery.monthTypeInformation("All Staff");
        departmentAppointments = monthTypeQuery.monthTypeInformation("Department Meeting");
        planningAppointments = monthTypeQuery.monthTypeInformation("Planning Session");
        debriefingAppointments = monthTypeQuery.monthTypeInformation("De-Briefing");
        scrumAppointments = monthTypeQuery.monthTypeInformation("Scrum Meeting");

        // add all the information to overall list
        addToList(allStaffAppointments);
        addToList(departmentAppointments);
        addToList(planningAppointments);
        addToList(debriefingAppointments);
        addToList(scrumAppointments);

        // display information to the reportTableView
        reportTableView.setItems(information);
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
