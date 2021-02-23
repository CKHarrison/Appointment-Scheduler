package utilities;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Switch Scene class. Allows the user to provide a scene view they want to switch to and handles the switching to that scene.
 */
public class SwitchScene {
    private static Stage stage;
    private static Parent scene;
    private static String baseName = "/view/";


    /**
     * switchScene. Allows the switching of a scene based on input parameters.
     * @param event handler that reacts to the javafx root event.
     * @param sceneName String name of the fxml screen the user wants to switch to
     * @throws IOException throws SQLException in case a database error occurs.
     */
    public static void switchScreen(ActionEvent event, String sceneName) throws IOException {
        String fullName = baseName + sceneName;
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(SwitchScene.class.getResource(fullName));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
