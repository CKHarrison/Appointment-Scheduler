package com.chrisharrison;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/** Main class. This class is the launch point for the appointment scheduler JavaFX application. */
public class Main extends Application {
    public static Connection conn;

    /** start method. This launches the JavaFX initial scene.
     * @param primaryStage Initial stage for the JavaFX application. */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../view/Login.fxml"));
        primaryStage.setTitle("Appointment Scheduler");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /** main method. This method is the entry point for the application and is responsible for launching it.
     * Sets the connection to the MySQL database to use for every other class and launches the application.
     * @param args  */
    public static void main(String[] args) throws SQLException {
        conn = DBConnection.startConnection();

        launch(args);
    }
}
