package utilities;


import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

/**
 * Generic Alert Class. Class that will create a general alert that can be customized to any situation that needs an alert.
 */
public class GenericAlert {
    private String title;
    private String contentText;
    private Alert.AlertType alertType;
    private Alert alert;

    /**
     * Generic Alert Constructor. Constructs a custom alert based on the user provided input, and alert type.
     * @param title String
     * @param contentText String
     * @param alertType AlertType
     */
    public GenericAlert(String title, String contentText, Alert.AlertType alertType) {
        this.title = title;
        this.contentText = contentText;
        this.alertType = alertType;
        this.alert =  new Alert(alertType);
    }

    /**
     * showAlert. Shows the custom alert and upon the user clicking ok, will switch the scene to user specified scene.
     * @param event event object to handle the event where the user presses the ok button.
     * @param screenToSwitch String of the fxml view the user wants to switch to.
     * @throws IOException throws IOException if one occurs.
     */
    public void showAlert(ActionEvent event, String screenToSwitch) throws IOException {
        alert.setTitle(title);
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
           SwitchScene.switchScreen(event, screenToSwitch);
        }
    }
}
