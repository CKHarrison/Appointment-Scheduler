package utilities.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.User;

import java.sql.SQLException;

/** UserDAO interface. UserDAO interface will help connect  User POJO class and user database table. */
public interface UserDAO {


    /**
     * getAllUsers(). Method to return all users from the database.
     * @return ObservableList<User>
     */
    @FXML
    public ObservableList<User> getAllUsers();

    /**
     * getUser. Returns a specific user from the database.
     * @param userID Integer
     * @return User
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public User getUser(int userID) throws SQLException;

    /**
     * getUser. Overloaded get user that returns a specific user from the database.
     * @param userName String
     * @return User
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @FXML
    public User getUser(String userName) throws SQLException;

}
