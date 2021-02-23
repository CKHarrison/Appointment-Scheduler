package utilities;


import com.chrisharrison.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MonthTypeQuery {

    Connection conn = Main.conn;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public MonthTypeQuery() throws SQLException {
    }

    public ObservableList<AppointmentStats> monthTypeInformation(String type) {
        String selectTypeInformation = "SELECT MONTHNAME(Start) as MONTH, type, COUNT(*) as COUNT FROM appointments WHERE TYPE=? GROUP BY MONTHNAME(Start)";
        ObservableList<AppointmentStats> appointmentStatsCollection = FXCollections.observableArrayList();
        try {

            QueryDatabase.setPreparedStatement(conn, selectTypeInformation);
            preparedStatement = QueryDatabase.getPreparedStatement();
            preparedStatement.setString(1, type);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String month = resultSet.getString("MONTH");
                int count = resultSet.getInt("COUNT");

                AppointmentStats appointmentStats = new AppointmentStats(month, type, count);

                appointmentStatsCollection.add(appointmentStats);

            }
        } catch (SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentStatsCollection;
    }

//    public String getMonth(ObservableList<>)

}
