package utilities;

/**
 * Appointment Stat class. Responsible for creating appointment stats generated from a query from the database.
 */
public class AppointmentStats {
    private String month;
    private String type;
    private int count;

    /**
     * AppointmentStats constructor. Instantiates instance of the AppointmentStats class.
     * @param month String
     * @param type String
     * @param count Integer
     */
    public AppointmentStats(String month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**
     * getMonth. Returns month of appointment.
     * @return String month.
     */
    public String getMonth() {
        return month;
    }

    /**
     * setMonth. Sets the month of an appointment.
     * @param month String
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * getType. Returns the type of an appointment.
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     * setType. Sets the type of an appointment.
     * @param type String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getCount. Returns the count of the particular appointment type.
     * @return Integer count.
     */
    public int getCount() {
        return count;
    }

    /**
     * setCount. Sets the count of an appointment type.
     * @param count Integer count.
     */
    public void setCount(int count) {
        this.count = count;
    }
}
