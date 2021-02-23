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
import utilities.DAO.AppointmentDAOImpl;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** ContactSchedule class. This class is responsible for controlling the ContactSchedule.fxml view. Displays a report showing the schedule for all contacts.  */
public class ContactSchedule implements Initializable {

    @FXML
    private TableView<Appointment> contactTableView;

    @FXML
    private TableColumn<Appointment, String> contactNameCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, String> dateCol;

    @FXML
    private TableColumn<Appointment, String> startCol;

    @FXML
    private TableColumn<Appointment, String> endCol;

    @FXML
    private TableColumn<Appointment, Integer> customerIDCol;

    @FXML
    private Button viewReportsBtn;

    @FXML
    private ObservableList<Appointment> firstContactAppointments;

    @FXML
    private ObservableList<Appointment> secondContactAppointments;

    @FXML
    private ObservableList<Appointment> thirdContactAppointments;

    @FXML
    private ObservableList<Appointment> allContactAppointmentsInOrder = FXCollections.observableArrayList();

    private AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();

    /** contactSchedule constructor. Responsible for initializing the class. Throws and SQL Exception to deal with database connectivity issues. */
    public ContactSchedule() throws SQLException {
    }

    /**addListToAllContactAppointments. This method uses a lambda forEach expression to compactly and efficiently
     *  add an observableList of appointments to another observableList.
     * @param listToAdd ObservableList<Appointment> lisToAdd. This parameter will be the list added to allContactAppointmentsInOrder ObservableList. */
    private void addListToAllContactAppointments(ObservableList<Appointment> listToAdd) {
        listToAdd.forEach(appointment -> allContactAppointmentsInOrder.add(appointment));
    }

    /** onActionViewReports. handles the event when the view reports button is clicked. Will switch scenes to ReportView.fxml.
     * @param event handles the event when the view reports button is clicked. */
    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ReportView.fxml");
    }

    /**  initialize. This method implements the Initializable interface. Responsible for grabbing all contacts from the database
     * and adding them to an overall list, and setting up the table view.
     * @param url used to resolve relative paths for root object.
     * @param resourceBundle localizes the root object*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // grab every contact's appointment from the database
            firstContactAppointments = appointmentDAO.getContactSpecificAppointment(1);
            secondContactAppointments = appointmentDAO.getContactSpecificAppointment(2);
            thirdContactAppointments = appointmentDAO.getContactSpecificAppointment(3);

            // add all contact information to an overall appointment list
            addListToAllContactAppointments(firstContactAppointments);
            addListToAllContactAppointments(secondContactAppointments);
            addListToAllContactAppointments(thirdContactAppointments);

            // set up and populate the table view
            contactTableView.setItems(allContactAppointmentsInOrder);
            contactNameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            appointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("startDateFormatted"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("startTimeFormatted"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("endTimeFormatted"));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        // handle any SQL errors
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
