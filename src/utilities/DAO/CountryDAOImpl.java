package utilities.DAO;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Country;
import utilities.QueryDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/** CountryDAO Implementation class of CountryDAO interface. This class allows for the retrieval of all country records from the database.  */

public class CountryDAOImpl implements CountryDAO {
    private final Connection conn = Main.conn;
    private final String selectAllCountries = "SELECT * FROM countries";
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;


    @FXML
    private ObservableList<Country> countries = FXCollections.observableArrayList();

    /** CountryDAOImpl constructor method. Instantiates the class and creates a prepared query and resultSet data structure */
    public CountryDAOImpl() throws SQLException {
        QueryDatabase.setPreparedStatement(conn, selectAllCountries);
        preparedStatement = QueryDatabase.getPreparedStatement();
        resultSet = preparedStatement.executeQuery();
    }

    /** getAllCountries() method.
     * collects all the Country records from the database.
     * @return ObservableArrayList<Country>
     * */
    @Override
    public ObservableList<Country> getAllCountries() {
        try{
            while(resultSet.next()) {
                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                LocalDate date = resultSet.getDate("Create_Date").toLocalDate();
                LocalTime time = resultSet.getTime("Create_Date").toLocalTime();
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                Country country = new Country(countryID, countryName, date, time,lastUpdate, createdBy );
                countries.add(country);
            }
        } catch(SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return countries;
    }

    /**
     * getCountry. Returns a specific country based from countryID.
     * @param countryID Integer
     * @return Country
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public Country getCountry(int countryID) throws SQLException {
        // initialize country variable and create prepare statement.
        Country country = null;
        String selectCountry = "SELECT * FROM countries WHERE Country_ID=" + countryID;
        QueryDatabase.setPreparedStatement(conn, selectCountry);
        PreparedStatement preparedStatement = QueryDatabase.getPreparedStatement();
        ResultSet resultSet = preparedStatement.executeQuery();
        // search database for results and construct country.
        while (resultSet.next()) {
            String countryName = resultSet.getString("Country");
            LocalDate date = resultSet.getDate("Create_Date").toLocalDate();
            LocalTime time = resultSet.getTime("Create_Date").toLocalTime();
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
             country = new Country(countryID, countryName, date, time,lastUpdate, createdBy );
        }
        return country;
    }
}
