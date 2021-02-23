package utilities.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.FirstLevelDivision;

/** FirstLevelDivision table interface.
 * This interface will help connect the firstLevelDivision POJO to the relevant controllers */
public interface FirstLevelDivisionDAO {
    /**
     * getAllDivisions. Return an observable list of all divisions in the database.
     * @return ObservableList<FirstLevelDivision>
     */
    @FXML
    public ObservableList<FirstLevelDivision> getAllDivisions();
}
