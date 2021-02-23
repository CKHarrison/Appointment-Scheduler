package utilities.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Appointment;

import java.sql.SQLException;
import java.time.LocalDateTime;

/** UserDAO interface. Acts as a go between with the Appointment Model and the Appointment table in the database. */
public interface AppointmentDAO {
    /**
     * getAllAppointments. Returns all appointments in the database as an observable list typed with Appointment.
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public ObservableList<Appointment> getAllAppointments() throws SQLException;

    /**
     * getAppointment. Returns a specific appointment specified by userID.
     * @param appointmentID Integer
     * @return Appointment
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public Appointment getAppointment(int appointmentID) throws SQLException;

    /** getAppointmentByType(). This returns a list of all appointments by specific type.
     * @param type String
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */

    @FXML
    public ObservableList<Appointment> getAppointmentByType(String type) throws SQLException;

    /**
     * getCurrentMonthAppointments. Returns an observable list of the current month's appointments.
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public ObservableList<Appointment> getCurrentMonthAppointments() throws SQLException;

    /**
     * getContactSpecificAppointment. Returns an observable list of appointments that is specific to a contact
     * @param contactID Integer
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public ObservableList<Appointment> getContactSpecificAppointment(int contactID) throws SQLException;

    /**
     * getUserSpecificAppointment. Returns an observable list of specific appointments to a user.
     * @param userID Integer
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public ObservableList<Appointment> getUserSpecificAppointment(int userID) throws SQLException;


    /**
     * addAppointment. Responsible for adding an appointment to the database.
     * @param title String
     * @param description String
     * @param location String
     * @param type String
     * @param startDateTime LocalDateTime
     * @param endDateTime LocalDateTime
     * @param createDate LocalDateTime
     * @param createdBy String
     * @param lastUpdate LocalDateTime
     * @param lastUpdatedBy String
     * @param customerID Integer
     * @param userID Integer
     * @param contactID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public void addAppointment(String title, String description, String location, String type,
                               LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createDate, String createdBy,
                               LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException;

    /**
     * updateAppointment. Updates a selected appointment and adds it to the database.
     * @param appointmentID Integer
     * @param title String
     * @param description String
     * @param location String
     * @param type String
     * @param start LocalDateTime
     * @param end LocalDateTime
     * @param customerID Integer
     * @param userID Integer
     * @param contactID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML void updateAppointment(int appointmentID, String title, String description, String location, String type,
                                 LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) throws SQLException;

    /**
     * deleteAppointment. Deletes a specific appointment by appointment ID.
     * @param appointmentID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public void deleteAppointment(int appointmentID) throws SQLException;

    /**
     * deleteAssociatedAppointments. Deletes all appointments that are associated with a customer.
     * @param customerID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public void deleteAssociatedAppointments(int customerID) throws SQLException;
}


