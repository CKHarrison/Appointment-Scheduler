package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import utilities.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** ReportView controller class. Responsible for displaying ReportView.fxml scene. */
public class ReportView implements Initializable {

    @FXML
    private Button monthTypeButton;

    @FXML
    private Button contactScheduleButton;

    @FXML
    private Button userScheduleBtn;

    @FXML
    private Button cancelButton;

    /** onActionUserSchedule. Handles the event where the user wants to see the UserScene.fxml screen and switches to it.
     * @param event handles the event where the user clicks the userSchedule button. */
    @FXML
    void onActionUserSchedule(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "UserSchedule.fxml");
    }

    /** onActionContactSchedule. Handles the event where the user wants to see the ContactSchedule.fxml screen and switches to it.
     * @param event handles the event where the user clicks the contactSchedule Button */
    @FXML
    void onActionContactSchedule(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "ContactSchedule.fxml");
    }

    /** onActionMonthAndType. Handles the event where the user wants to see the MonthTypeBrief.fxml screen and switches to it.
     * @param event handles the event where the user clicks the month type brief button. */
    @FXML
    void onActionMonthAndType(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "MonthTypeBrief.fxml");
    }

    /** onActionCancel. Handles the event where the user wants to go back to the main menu.
     * @param event handles the event where the user clicks the cancel/ main menu button. */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        SwitchScene.switchScreen(event, "MainMenu.fxml");
    }

    /** initialize. Implements the initializable interface. Sets up the ReportView controller. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
