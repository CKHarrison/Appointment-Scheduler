package utilities.DAO;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Contact;
import utilities.QueryDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**  ContactDAOImpl class. Implementation of Contact DAO interface for interaction between database and Contact POJO */
public class ContactDAOImpl {

    private String selectAllContacts = "SELECT * FROM contacts";
    Connection conn = Main.conn;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * Constructor method to instantiate ContactDAOImpl.
     * @throws SQLException
     */
    public ContactDAOImpl() throws SQLException {
        QueryDatabase.setPreparedStatement(conn, selectAllContacts);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
    }

    /** getAllContacts().
     * Method to return an ObservableList of all contacts
     * @return contacts
     */
    public ObservableList<Contact> getAllContacts() {
        int contactID;
        String contactName;
        String email;

        try {
            while(resultSet.next()) {
                contactID = resultSet.getInt("Contact_ID");
                contactName = resultSet.getString("Contact_Name");
                email = resultSet.getString("Email");

                Contact contact = new Contact(contactID, contactName, email);
                contacts.add(contact);
            }
        } catch(SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return contacts;
    }
    /**
     * getContact. Returns specific contact.
     * @param contactId Integer
     * @return Contact
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public Contact getContact(int contactId) throws SQLException {
        String selectContact = "SELECT * FROM contacts WHERE Contact_ID=" + String.valueOf(contactId);
        QueryDatabase.setPreparedStatement(conn,selectContact);
        PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
        ResultSet resultSet = preparedStatement.executeQuery();


        while(resultSet.next()) {
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");
            String contactEmail = resultSet.getString("Email");
            Contact foundContact = new Contact(contactID, contactName, contactEmail);
            return foundContact;
        }
        return null;
    }
}
