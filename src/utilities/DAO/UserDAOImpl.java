package utilities.DAO;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.User;
import utilities.QueryDatabase;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * UserDAOImpl class. Implements UserDAO interface. Responsible for retrieving users from the database.
 */
public class UserDAOImpl implements UserDAO{
    private String selectAllUsers = "Select * FROM users";
    Connection conn = Main.conn;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    private ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * UserDAOImpl. Constructor method to instantiate UserDAOImpl.
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public UserDAOImpl() throws SQLException {
        QueryDatabase.setPreparedStatement(conn, selectAllUsers);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
    }

    /**
     * getAllUsers(). Method to return all users from the database.
     * @return ObservableList<User>
     */
    @Override
    public ObservableList<User> getAllUsers() {
        int userID;
        String userName;
        String password;
        LocalDateTime createDateTime;
        String createdBy;
        Timestamp lastUpdate;
        String lastUpdatedBy;
        // gather results and create a user. Add to an overall list.
        try {
            while(resultSet.next()) {
                userID = resultSet.getInt("User_ID");
                userName = resultSet.getString("User_Name");
                password = resultSet.getString("Password");
                createDateTime = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                createdBy = resultSet.getString("Created_By");
                lastUpdate = resultSet.getTimestamp("Last_Update");
                lastUpdatedBy = resultSet.getString("Last_Updated_By");

                User user = new User(userID, userName, password, createDateTime, createdBy, lastUpdate, lastUpdatedBy);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    /**
     * getUser. Returns a specific user from the database.
     * @param userID Integer
     * @return User
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public User getUser(int userID) throws SQLException {
        // set query string and connect ot the database for user
        String selectUser = "SELECT * FROM users WHERE User_ID=" + userID;

        QueryDatabase.setPreparedStatement(conn, selectUser);
        PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
        ResultSet resultSet = preparedStatement.executeQuery();
        // gather results and build into a user
        while(resultSet.next()) {
            String userName = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");
            LocalDateTime createDateTime = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");

            User foundUser = new User(userID, userName, password, createDateTime, createdBy, lastUpdate, lastUpdatedBy);
            return foundUser;
        }
        // if no user is found return null
        return null;
    }
    /**
     * getUser. Overloaded get user that returns a specific user from the database.
     * @param userName String
     * @return User
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    @Override
    public User getUser(String userName) throws SQLException {
        // Set a select statement a query the database
        String selectUser = "SELECT * FROM users WHERE User_Name=" + userName;

        QueryDatabase.setPreparedStatement(conn, selectUser);
        PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
        ResultSet resultSet = preparedStatement.executeQuery();
        // build and return user if it exists, if not return null
        while(resultSet.next()) {

            int userID = resultSet.getInt("User_ID");
            String password = resultSet.getString("Password");
            LocalDateTime createDateTime = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");

            User foundUser = new User(userID, userName, password, createDateTime, createdBy, lastUpdate, lastUpdatedBy);
            return foundUser;
        }
        return null;
    }
}
