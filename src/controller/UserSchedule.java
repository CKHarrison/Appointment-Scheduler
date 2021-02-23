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

/** UserSchedule controller class. Handles the UserSchedule.fxml view and displays all the information about user schedules.
 * Implements the Initializable interface.  */
public class UserSchedule implements Initializable {

    @FXML
    private TableView<Appointment> userTableView;

    @FXML
    private TableColumn<Appointment, String> userCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCol;

    @FXML
    private TableColumn<Appointment, String > titleCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, String > dateCol;

    @FXML
    private TableColumn<Appointment, String > startCol;

    @FXML
    private TableColumn<Appointment, String > endCol;

    @FXML
    private TableColumn<Appointment, Integer> customerIDCol;

    @FXML
    private Button viewReportsBtn;

    @FXML
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Appointment> firstUser;
    @FXML
    private ObservableList<Appointment> secondUser;

    private AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();

    /** UserSchedule constructor. Instantiates UserSchedule class and throws SQLException if there are database errors. */
    public UserSchedule() throws SQLException {
    }

    /** addListToAllContactAppointments. Uses forEach lambda expression to add a specific user's list of appointments
     * from a list to an overall list
     * of appointments. forEach lambda compactly and efficiently adds each appointment to the overall list. Using this lambda
     * reduces possibility for bugs.
     * @param listToAdd ObservableArrayList<Appointments> list of appointments to add to overall list. */
    private void addListToAllContactAppointments(ObservableList<Appointment> listToAdd) {
        listToAdd.forEach(appointment -> allAppointments.add(appointment));
    }

    /** onActionViewReports. Handles the action where the user wants to go back to view all reports scene.
     * @param event handles the event where the user clicks Back to Report View button.  */
    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ReportView.fxml");
    }

    /** UpdateCustomer controller class. Implements the Initializable interface. Grabs all user's list appointments from the database
     * and combines them to an overall list for display on the table view.
     * @param url used to resolve relative paths for root object.
     * @param resourceBundle localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Grab all user specific users appointments
            firstUser = appointmentDAO.getUserSpecificAppointment(1);
            secondUser = appointmentDAO.getUserSpecificAppointment(2);
            // add each user's list to overall list
            addListToAllContactAppointments(firstUser);
            addListToAllContactAppointments(secondUser);
            // set up table view
            userTableView.setItems(allAppointments);
            userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
            appointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

            dateCol.setCellValueFactory(new PropertyValueFactory<>("startDateFormatted"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("startTimeFormatted"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("endTimeFormatted"));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        } catch (SQLException e) {
            System.out.println("There was an error" + e.getMessage());
            e.printStackTrace();
        }

    }
}
