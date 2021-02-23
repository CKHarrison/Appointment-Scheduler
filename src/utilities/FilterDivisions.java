package utilities;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Country;
import model.FirstLevelDivision;
import utilities.DAO.CountryDAOImpl;
import utilities.DAO.FirstLevelDivisionDAOImpl;

import java.sql.SQLException;

/**
 * FilterDivisions class. Responsible for filtering divisions based on what country the user has selected.
 */
public class FilterDivisions {
    private FirstLevelDivisionDAOImpl firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();
    private CountryDAOImpl countryDAO = new CountryDAOImpl();

    @FXML
    private ObservableList<Country> allCountries = countryDAO.getAllCountries();

    @FXML
    private ObservableList<FirstLevelDivision> allDivisions = firstLevelDivisionDAO.getAllDivisions();

    /**
     * FilterDivisions Constructor. Responsible for instantiating the FilterDivision object.
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public FilterDivisions() throws SQLException {
    }

    /**Filtered Divisions method. Takes a list off all divisions and then filters them based on country id.
     * @param id Integer
     * @return ObservableList<FirstLevelDivision>*/
    public ObservableList<FirstLevelDivision> FilterDivisions(int id) throws SQLException {
        // grab all divisions ids and then filter them based on the country id
        ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList();
        for(FirstLevelDivision firstLevelDivision : allDivisions) {
            if (firstLevelDivision.getCountryID()  == id) {
                filteredDivisions.add(firstLevelDivision);
            }
        }
        return filteredDivisions;
    }
}
