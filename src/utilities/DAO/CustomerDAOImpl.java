package utilities.DAO;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Customer;
import utilities.QueryDatabase;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * CustomerDAOImpl class. Implements the CustomerDAO Interface. Responsible for adding, update, retrieving, and deleting
 * customers from the database.
 */
public class CustomerDAOImpl implements CustomerDAO{
    private Connection conn = Main.conn;
    private final String selectAllCustomers = "SELECT * FROM customers";
    private String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private String getCustomerStatement = "SELECT * from customers where Customer_ID=";
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * CustomerDAOImpl constructor. Responsible for instantiating instance of CustomerDAOImpl.
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public CustomerDAOImpl() throws SQLException {

    }

    /**
     * createCustomer. Creates a customer Pojo object for use in other class methods.
     * @return Customer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    private Customer createCustomer() throws SQLException {
        int customerID = resultSet.getInt("Customer_ID");
        String customerName = resultSet.getString("Customer_Name");
        String customerAddress = resultSet.getString("Address");
        String postalCode = resultSet.getString("Postal_Code");
        String phone = resultSet.getString("Phone");
        LocalDateTime createDateTime = resultSet.getTimestamp("Create_Date").toLocalDateTime();
        LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
        String createdBy = resultSet.getString("Created_By");
        String lastUpdatedBy = resultSet.getString("Last_Updated_By");
        int divisionID = resultSet.getInt("Division_ID");

        Customer customer = new Customer(customerID, customerName, customerAddress, postalCode, phone,
                createDateTime, createdBy, lastUpdate, lastUpdatedBy, divisionID);
        return customer;
    }


    /**
     * getAllCustomers. Retrieves an observable list of all customers from the database.
     * @return ObservableList<Customer>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        QueryDatabase.setPreparedStatement(conn, selectAllCustomers);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
        try {
            while(resultSet.next()) {

                Customer customer = createCustomer();
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * addCustomer. Adds a customer model object to the database.
     * @param customerName String
     * @param address String
     * @param zipcode String
     * @param phone String
     * @param createdBy String
     * @param lastUpdatedBy String
     * @param divisionID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public void addCustomer(String customerName, String address, String zipcode, String phone, String createdBy, String lastUpdatedBy, int divisionID) throws SQLException {
       // Query the database with insert statement. and add customer
        QueryDatabase.setPreparedStatement(conn, insertStatement);
        preparedStatement = QueryDatabase.getPreparedStatement();
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, zipcode);
        preparedStatement.setString(4, phone);
        preparedStatement.setString(5, createdBy);
        preparedStatement.setString(6, lastUpdatedBy);
        preparedStatement.setInt(7, divisionID);

        preparedStatement.execute();
    }

    /**
     * getCustomer. Returns a customer from the database.
     * @param customerID Integer
     * @return Customer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public Customer getCustomer(int customerID) throws SQLException {
        // create query string and return customer if it exists
        String customerIDString = String.valueOf(customerID);
        String getCustomerStatement = "SELECT * from customers where Customer_ID=";
        getCustomerStatement += customerIDString;
        QueryDatabase.setPreparedStatement(conn, getCustomerStatement);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
        try {
            while(resultSet.next()) {
                Customer customer = createCustomer();

                return customer;
            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * updateCustomer. Updates and existing customer and save it to the database.
     * @param customerID Integer
     * @param customerName String
     * @param address String
     * @param zipcode String
     * @param phone String
     * @param divisionID Integer
     * @param lastUpdatedBy String
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public void updateCustomer(int customerID, String customerName, String address, String zipcode, String phone, int divisionID, String lastUpdatedBy) throws SQLException {
        //Create query string and update customer
        Customer customerToUpdate = getCustomer(customerID);
        LocalDateTime customerCreatedDate = customerToUpdate.getCreateDateTime();
        String updateStatement = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=?, Create_Date=?, Last_Update=?, Last_Updated_By=? WHERE Customer_ID=?";
        QueryDatabase.setPreparedStatement(conn, updateStatement);
       try {
           //insert parameters to query string
           preparedStatement = QueryDatabase.getPreparedStatement();
           preparedStatement.setString(1, customerName);
           preparedStatement.setString(2, address);
           preparedStatement.setString(3, zipcode);
           preparedStatement.setString(4, phone);
           preparedStatement.setInt(5, divisionID);
           preparedStatement.setDate(6, Date.valueOf(customerCreatedDate.toLocalDate()));
           preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
           preparedStatement.setString(8, lastUpdatedBy);
           preparedStatement.setInt(9, customerID);

           preparedStatement.execute();
       } catch (SQLException e ) {
           System.out.println("There was an error: " + e.getMessage());
           e.printStackTrace();
       }


    }

    /**
     *  deleteCustomer. Deletes a specific customer based on customer ID and deletes associated appointments.
     * @param customerID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public void deleteCustomer(int customerID) throws SQLException {
        String deleteStatement = "DELETE FROM customers WHERE Customer_ID =";
        deleteStatement += customerID;
        //create a Appointment DAO object to delete associated appointments
        AppointmentDAOImpl appointmentDAO =  new AppointmentDAOImpl();
        QueryDatabase.setPreparedStatement(conn, deleteStatement);
        try {
            PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
            //First delete any associated appointments
            appointmentDAO.deleteAssociatedAppointments(customerID);
            // Then delete the customer
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
