package utilities;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB connection class. Responsible for creating a connection to the server that the database is hosted on.
 */
public class DBConnection {
    // JDBC URL parts
    private static final String protocol = "jdbc";
    public static final String vendorName = ":mysql:";
    public static final String ipAddress  = "//wgudb.ucertify.com/WJ05Pk7";

    //JDC URL
    public static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver and Connection interface set up
    public static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    public static Connection conn = null;
    public static final String username = "U05Pk7";
    public static final String password = "53688567751";

    /**
     * startConnection. Responsible for connecting to the database.
     * @return Connection object
     */
    public static Connection startConnection() {
        // try to connect to the database
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL,username, password);
            System.out.println("Connected to database!");
        // if the connection fails, let the user know.
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * closeConnection. Closes the connection to the database.
     */
    public static void closeConnection() {
        // try to close the connection to the database, and if an error occurs let the user know.
        try {
            conn.close();
            System.out.println("Connection closed");
        } catch(SQLException e) {
            System.out.println("There was an error closing the connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
