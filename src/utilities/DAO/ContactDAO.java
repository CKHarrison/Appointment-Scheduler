package utilities.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Contact;

import java.sql.SQLException;

/** Interface between the database and the POJO representation of the Contact table */
public interface ContactDAO {

    /**
     * getAllContacts. Return an observable list of all contacts from the database.
     * @return ObservableList<Contact>
     */
    @FXML
    public ObservableList<Contact> getAllContacts() ;

    /**
     * getContact. Returns specific contact.
     * @param contactId Integer
     * @return Contact
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public Contact getContact(int contactId) throws SQLException;
}
