package model;


import java.sql.Timestamp;
import java.time.LocalDateTime;

/** User model class. User POJO representation of the User table in the database */
public class User {
    private int userID;
    private String userName;
    private String password;
    private LocalDateTime createDateTime;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    /**
     * User constructor. Instantiates an instance of the user class.
     * @param userID Integer
     * @param userName String
     * @param password String
     * @param createDateTime LocalDateTime
     * @param createdBy String
     * @param lastUpdate TimeStamp
     * @param lastUpdatedBy String
     */
    public User(int userID, String userName, String password, LocalDateTime createDateTime, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.createDateTime = createDateTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * getUserID. Returns the user ID of the user.
     * @return userID Integer.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * setUserID. Sets the userID.
     * @param userID Integer.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * getUserName. Returns the user's username.
     * @return String userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName. Sets the name of the user.
     * @param userName String.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getPassword. Returns the password of the user.
     * @return String password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword. Set's the password of the user.
     * @param password String.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getCreateDateTime. Gets the creation date and time of the user.
     * @return LocalDateTime user.
     */
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    /**
     * setCreateDateTime. Sets the creation date and time of the user.
     * @param createDateTime LocalDateTime.
     */
    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * getCreatedBy. Returns the person who created the user.
     * @return String createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * setCreatedBy. Sets the person who created the user.
     * @param createdBy String.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * getLastUpdate. Returns the date and time of the last update of the user.
     * @return Timestamp lastUpdate.
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * setLastUpdate. Sets the date and time of the user's last update.
     * @param lastUpdate Timestamp.
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * getLastUpdatedBy. Returns the person who last updated the user.
     * @return String lastUpdatedBy.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * setLastUpdatedBy. Set the person who last updated the user.
     * @param lastUpdatedBy String
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * toString. Overrides the default toString method that shows the userName.
     * @return String userName.
     */
    @Override
    public String toString() {
        return userName;
    }
}
