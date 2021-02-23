package utilities.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Customer;

import java.sql.SQLException;

/** Customer interface to interact with the POJO Customer representation and the database customer table. */
public interface CustomerDAO {

    /**
     * getAllCustomers. Returns an observable list of all customers in the database.
     * @return ObservableList<Customer>
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public ObservableList<Customer> getAllCustomers() throws SQLException;

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
    @FXML
    public void addCustomer(String customerName, String address, String zipcode, String phone, String createdBy, String lastUpdatedBy, int divisionID) throws SQLException;

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
    @FXML
    public void updateCustomer(int customerID, String customerName, String address, String zipcode, String phone, int divisionID, String lastUpdatedBy) throws SQLException;

    /**
     *  deleteCustomer. Deletes a specific customer based on customer ID and deletes associated appointments.
     * @param customerID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public void deleteCustomer(int customerID) throws SQLException;

    /**
     * getCustomer. Returns a customer from the database.
     * @param customerID Integer
     * @return Customer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public Customer getCustomer(int customerID) throws SQLException;
}

