package utilities.DAO;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.FirstLevelDivision;
import utilities.QueryDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * FirstLevelDivisionDAOImpl class. Implements FirstLevelDivisionDAO interface. Can retrieve all divisions from the database
 * or a single specific one.
 */
public class FirstLevelDivisionDAOImpl implements FirstLevelDivisionDAO {
    private final Connection conn = Main.conn;
    private final String selectAllDivisions = "SELECT * FROM first_level_divisions;";
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    @FXML
    private ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();

    /** FirstLevelDivision constructor. Sets up connection to database and creates prepared
     * statement and resultSet objects.
     * @throws throws SQLException in case a database error occurs.*/
    public FirstLevelDivisionDAOImpl() throws SQLException {
        QueryDatabase.setPreparedStatement(conn, selectAllDivisions);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
    }

    /**
     * getAllDivisions. Return an observable list of all divisions in the database.
     * @return ObservableList<FirstLevelDivision>
     */
    public ObservableList<FirstLevelDivision> getAllDivisions() {
        // Gather all divisions from the database convert the to division POJO object and add them to overall list.
        try {
            while(resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                LocalDate createDate = resultSet.getDate("Create_Date").toLocalDate();
                LocalTime createTime = resultSet.getTime("Create_Date").toLocalTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int countryID = resultSet.getInt("Country_ID");

                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(divisionID, division, createDate, createTime, createdBy, lastUpdate, lastUpdatedBy, countryID);

                allFirstLevelDivisions.add(firstLevelDivision);
            }
        } catch(SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return allFirstLevelDivisions;
    }

    /**
     * getFirstLevelDivision. Return a specified first level division.
     * @param divisionID Integer
     * @return FirstLevelDivision
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public FirstLevelDivision getFirstLevelDivision(int divisionID) throws SQLException {
        // Create query and select statement
        String selectDivision = "SELECT * FROM first_level_divisions where Division_ID=" + divisionID;
        QueryDatabase.setPreparedStatement(conn, selectDivision);
        PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
        ResultSet resultSet = preparedStatement.executeQuery();
        // initialize first level division
        FirstLevelDivision firstLevelDivision = null;
        // gather results and populate division
        while(resultSet.next()) {
            String division = resultSet.getString("Division");
            LocalDate createDate = resultSet.getDate("Create_Date").toLocalDate();
            LocalTime createTime = resultSet.getTime("Create_Date").toLocalTime();
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int countryID = resultSet.getInt("Country_ID");

            firstLevelDivision = new FirstLevelDivision(divisionID, division, createDate, createTime, createdBy, lastUpdate, lastUpdatedBy,countryID);
        }
        return firstLevelDivision;
    }
}
