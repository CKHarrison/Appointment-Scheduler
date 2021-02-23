package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import utilities.DAO.CustomerDAOImpl;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** ViewCustomers controller class. Handles ViewCustomers.fxml scene that allows the user to view, edit and delete, all available customers.
 * Implements Initializable interface.  */
public class ViewCustomers implements Initializable {

    public static Customer customerToModify;
    private CustomerDAOImpl customerDAO = new CustomerDAOImpl();

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button modifyCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TableView<Customer> customerTblView;

    @FXML
    private TableColumn<Customer, Integer> customerIDCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, String> zipCodeCol;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableColumn<Customer, String> createDateCol;

    @FXML
    private TableColumn<Customer, String> createdByCol;

    @FXML
    private TableColumn<Customer, LocalTime> lastUpdateCol;

    @FXML
    private TableColumn<Customer, String> lastUpdateByCol;

    @FXML
    private TableColumn<Customer, Integer> divisionIDCol;

    @FXML
    private Button mainMenuButton;

    /** ViewCustomers constructor. Responsible for instantiating the ViewCustomer class. Throws SQL Exception if any database
     * errors occur during startup. */
    public ViewCustomers() throws SQLException {
    }

    /** onActionAddCustomer. Handles the action to allow the user to switch the screen to the AddCustomer.fxml.
     * @param event handles the event where the user clicks on the Add Customer button. */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "AddCustomer.fxml");
    }

    /** onActionDeleteCustomer. Handles the event where a user selects a customer and wants to delete them. Displays an alert
     * asking the user for confirmation to delete the user. If yes, first all appointments associated to the user is deleted from the database,
     * then the user is deleted. If no, the user is not deleted.
     * @param event handels the event where the user clicks on the delete customer button. */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws IOException, SQLException {
        Customer customerToDelete = customerTblView.getSelectionModel().getSelectedItem();
        Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmation.setTitle("Delete Confirmation");
        String confirmationContext = "Are you sure you want to delete " + customerToDelete.getCustomerName() + "?\n" +
                "All associated appointments with " + customerToDelete.getCustomerName() + " will also be deleted";
        deleteConfirmation.setContentText(confirmationContext);
        Optional<ButtonType> result = deleteConfirmation.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            customerDAO.deleteCustomer(customerToDelete.getCustomerID());
            SwitchScene.switchScreen(event, "ViewCustomers.fxml");
        }
    }

    /** onActionMainMenu. Handles the action where the user wants to switch the screen back to the MainMenu.fxml.
     * @param event handles the event where the user clicks the Main Menu button. */
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "MainMenu.fxml");

    }

    /** onActionModifyCustomer. Handles the action where the user selects a customer to modify, and switches the scene to
     * ModifyCustomer.fxml view.
     * @param event handles the event where the user clicks on the Modify Customer button*/
    @FXML
    void onActionModifyCustomer(ActionEvent event) throws IOException {
        customerToModify = customerTblView.getSelectionModel().getSelectedItem();
        SwitchScene.switchScreen(event, "UpdateCustomer.fxml");
    }

    /** initialize. Initializes the ViewCustomer scene and populates the table view displaying all customers. Implements the
     * Initializeable interface.
     * @param url used to resolve relative paths for root object.
     * @param resourceBundle localizes the root object */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Grab all customers from the database and populate the table view.
            CustomerDAOImpl customerDAO = new CustomerDAOImpl();
            customerTblView.setItems(customerDAO.getAllCustomers());
            customerIDCol.setCellValueFactory( new PropertyValueFactory<>("customerID"));
            customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            zipCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            createDateCol.setCellValueFactory(new PropertyValueFactory<>("formattedCreateDate"));
            createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("formattedLastUpdate"));
            lastUpdateByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
            divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

            customerTblView.getSelectionModel().selectFirst();

        // print any errors to the console if they occur
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}