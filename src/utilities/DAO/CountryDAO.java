package utilities.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Country;

/** Country table interface interface.
 * This interface will help connect the country POJO to the relevant controllers */
public interface CountryDAO {
    /**
     * getAllCountries. Returns an ObservableList of all countries.
     * @return ObservableList<Country>
     */
    @FXML
    public ObservableList<Country> getAllCountries();
}
