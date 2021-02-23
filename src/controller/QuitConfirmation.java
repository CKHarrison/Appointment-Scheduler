package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** QuitConfirmation class. Responsible for letting the user quit the application if they choose. */
public class QuitConfirmation implements Initializable {

    @FXML
    private Button noButton;

    @FXML
    private Button yesButton;

    /** onActionNoButton. Handles the event that the user doesn't want quit. Will go back to MainMenu.fxml.
     * @param event handles the event where the user clicks the No button. */
    @FXML
    void onActionNoButton(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "MainMenu.fxml");


    }
    /** onActionYesButton. Handles the event where the user wants to exit the application. Shuts down application cleanly.
     * @param event handles the event where the user clicks the yes button. */
    @FXML
    void onActionYesButton(ActionEvent event) throws IOException {
        System.exit(0);
    }

    /** initialize. Implements the initializable interface. Sets up the QuitConfirmationController. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
