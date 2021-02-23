package utilities.DAO;


import com.chrisharrison.Main;
import controller.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Appointment;
import utilities.QueryDatabase;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * AppointmentDAO Implementation class. Implements the methods in the AppointmentDAO Interface.
 */
public class AppointmentDAOImpl implements AppointmentDAO {
    private Connection conn = Main.conn;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String selectAllAppointments = "SELECT * FROM appointments";

    @FXML
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();


    @FXML
    ObservableList<Appointment> currentMonthAppointments = FXCollections.observableArrayList();

    /**
     * AppointmentDAOImpl constructor. Responsible for instantiating an instance of the AppointmentDAOImpl class. Throws
     * a SQL Exception error if a database error occurs.
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public AppointmentDAOImpl() throws SQLException {
    }

    /**
     * getAllAppointments. Returns an ObservableList of all appointments in the database.
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        // set up a query to the database and get results
        QueryDatabase.setPreparedStatement(conn, selectAllAppointments);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
        // create an appointment from the information and add to an overall list
        try {
            while(resultSet.next()) {
                Appointment appointment = getAppointmentInformation();
                appointments.add(appointment);
            }
        } catch (SQLException e ) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }


    /**
     * getCurrentMonthAppointments. Returns an observable list of the current month's appointments.
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public ObservableList<Appointment> getCurrentMonthAppointments() throws SQLException {
        // Create a select statement and query the database
        String getCurrentMonthAppointments = "Select * from appointments WHERE MONTH(start)=MONTH(current_date()) AND YEAR(start)=year(current_date())";
        QueryDatabase.setPreparedStatement(conn, getCurrentMonthAppointments);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
        // Construct appointment out of results and add to overall list.
        try {
            while(resultSet.next()) {
                Appointment appointment = getAppointmentInformation();
                currentMonthAppointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return currentMonthAppointments;
    }

    /**
     * getAppointment. Returns a specific appointment specified by userID.
     * @param appointmentID Integer
     * @return Appointment
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public Appointment getAppointment(int appointmentID) throws SQLException {
        // Create a select statement and query the database
        String selectAppointmentStatement = "SELECT * FROM appointments WHERE Appointment_ID =" + String.valueOf(appointmentID);
        QueryDatabase.setPreparedStatement(conn, selectAppointmentStatement);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
        // return appointment information in structure of appointment model
        try {
            while(resultSet.next()) {
                return getAppointmentInformation();
            }
        } catch (SQLException e) {
            System.out.println("There was an error:" + e.getMessage());
            e.printStackTrace();
        }
       // return null if none exist
        return null;
    }

    /** getAppointmentByType(). This returns a list of all appointments by specific type.
     * @param type String
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public ObservableList<Appointment> getAppointmentByType(String type) throws SQLException {
        // Create a select statement of appointment by type and query the database.
        ObservableList<Appointment> appointmentsByType = FXCollections.observableArrayList();
        String selectAppointmentByType = "SELECT * FROM appointments where type=?";
        QueryDatabase.setPreparedStatement(conn, selectAppointmentByType);
        preparedStatement = QueryDatabase.getPreparedStatement();
        preparedStatement.setString(1,type);
        resultSet = preparedStatement.executeQuery();
        // get appointment information, construct it and add to overall list
        try {
            while(resultSet.next()) {
                Appointment appointment = getAppointmentInformation();
                appointmentsByType.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return appointmentsByType;
    }

    /**
     * getContactSpecificAppointment. Returns an observable list of appointments that is specific to a contact
     * @param contactID Integer
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public ObservableList<Appointment> getContactSpecificAppointment(int contactID) throws SQLException {
        // create select statement and query the database
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
        String selectContactAppointment = "SELECT * FROM appointments WHERE Contact_ID=? ORDER BY start ASC";
        QueryDatabase.setPreparedStatement(conn, selectContactAppointment);
        preparedStatement = QueryDatabase.getPreparedStatement();
        preparedStatement.setInt(1, contactID);
        resultSet = preparedStatement.executeQuery();

        // get results and construct appointment and add to overall list.
        while (resultSet.next()) {
            Appointment appointment = getAppointmentInformation();
            contactAppointments.add(appointment);
        }
        return contactAppointments;
    }

    /**
     * getUserSpecificAppointment. Returns an observable list of specific appointments to a user.
     * @param userID Integer
     * @return ObservableList<Appointment>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public ObservableList<Appointment> getUserSpecificAppointment(int userID) throws SQLException {
        // create select statement and query the database
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        String selectUserAppointments = "SELECT * FROM appointments WHERE User_ID=? ORDER BY START ASC";
        QueryDatabase.setPreparedStatement(conn, selectUserAppointments);
        preparedStatement = QueryDatabase.getPreparedStatement();
        preparedStatement.setInt(1, userID);
        resultSet = preparedStatement.executeQuery();
        // While gathering results create appointment and add to overall list
        while (resultSet.next()) {
            Appointment appointment = getAppointmentInformation();
            userAppointments.add(appointment);
        }
         return userAppointments;
    }

    /**
     * getAppointmentInformation. Method builds out Appointment object and returns it.
     * @return Appointment */
    private Appointment getAppointmentInformation() throws SQLException {
        // gather information
        int appointmentID = resultSet.getInt("Appointment_ID");
        String title = resultSet.getString("Title");
        String description = resultSet.getString("Description");
        String location = resultSet.getString("Location");
        String type = resultSet.getString("Type");
        LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
        LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
        LocalDateTime creatDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
        String createdBy = resultSet.getString("Created_By");
        LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
        String lastUpdatedBy = resultSet.getString("Last_Updated_By");
        int customerID = resultSet.getInt("Customer_ID");
        int userID = resultSet.getInt("User_ID");
        int contactID = resultSet.getInt("Contact_ID");
        // create new appointment and return it
        Appointment appointment = new Appointment(appointmentID, title, description, location, type, start, end, creatDate, createdBy,
                lastUpdate, lastUpdatedBy, customerID, userID, contactID);
        return appointment;
    }

    /**
     * addAppointment. Responsible for adding an appointment to the database.
     * @param title String
     * @param description String
     * @param location String
     * @param type String
     * @param start LocalDateTime
     * @param end LocalDateTime
     * @param createDate LocalDateTime
     * @param createdBy String
     * @param lastUpdate LocalDateTime
     * @param lastUpdatedBy String
     * @param customerID Integer
     * @param userID Integer
     * @param contactID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public void addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                               LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy,
                               int customerID, int userID, int contactID) throws SQLException {
        // create query and prepare statement
        String insertAppointment = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        QueryDatabase.setPreparedStatement(conn, insertAppointment);
        preparedStatement = QueryDatabase.getPreparedStatement();
        // add parameters to select statement
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
        preparedStatement.setTimestamp(7, Timestamp.valueOf(createDate));
        preparedStatement.setString(8, createdBy);
        preparedStatement.setTimestamp(9, Timestamp.valueOf(lastUpdate));
        preparedStatement.setString(10, lastUpdatedBy);
        preparedStatement.setInt(11, customerID);
        preparedStatement.setInt(12, userID);
        preparedStatement.setInt(13, contactID);

        preparedStatement.execute();
        //check rows affected
        if (preparedStatement.getUpdateCount() > 0) {
            System.out.println("Appointment was inserted");
        } else {
            System.out.println("Nothing was inserted");
        }
    }

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
    @Override
    public void updateAppointment(int appointmentID, String title, String description, String location, String type,
                                  LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) throws SQLException {
        // create prepare statement and database connection
        String updateStatement = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=?, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?";
        QueryDatabase.setPreparedStatement(conn, updateStatement);
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdatedBy = LoginController.userName;
        try {
            // add parameters to update statement
            PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(lastUpdate));
            preparedStatement.setString(8, lastUpdatedBy);
            preparedStatement.setInt(9, customerID);
            preparedStatement.setInt(10, userID);
            preparedStatement.setInt(11, contactID);
            preparedStatement.setInt(12, appointmentID);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * deleteAppointment. Deletes a specific appointment by appointment ID.
     * @param appointmentID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public void deleteAppointment(int appointmentID) throws SQLException {
        StringBuilder deleteStatement = new StringBuilder();
        deleteStatement.append("DELETE FROM appointments WHERE Appointment_ID=");
        deleteStatement.append(appointmentID);
        QueryDatabase.setPreparedStatement(conn, deleteStatement.toString());

        try {
            PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("There was an exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * deleteAssociatedAppointments. Deletes all appointments that are associated with a customer.
     * @param customerID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public void deleteAssociatedAppointments(int customerID) throws SQLException {
        String deleteAppointmentsStatement = "DELETE FROM appointments WHERE Customer_ID=";
        deleteAppointmentsStatement += customerID;

        QueryDatabase.setPreparedStatement(conn, deleteAppointmentsStatement);

        try {
            PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
