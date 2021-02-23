package model;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/** First Level Division Class model class. POJO representation of the First Level Division Table */
public class FirstLevelDivision {
    private int divisionID;
    private String division;
    private LocalDate createDate;
    private LocalTime createTime;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private int countryID;

    /**
     * FirstLevelDivision constructor. Instantiates the FirstLevelDivision class.
     * @param divisionID Integer
     * @param division String
     * @param createDate LocalDate
     * @param createTime LocalTime
     * @param createdBy String
     * @param lastUpdate Timestamp
     * @param lastUpdateBy String
     * @param countryID Integer
     */
    public FirstLevelDivision(int divisionID, String division, LocalDate createDate, LocalTime createTime, String createdBy, Timestamp lastUpdate, String lastUpdateBy, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.createDate = createDate;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.countryID = countryID;
    }

    /**
     * getDivisionID. Returns division ID associated with the division.
     * @return divisionID Integer
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * setDivisionID. Sets the divisionID of the division.
     * @param divisionID Integer.
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * getDivision. Returns the division name.
     * @return String division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * setDivision. Sets the division name of the division.
     * @param division String
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * getCreateDate. Returns creation date of the divison.
     * @return LocalDate createDate
     */
    public LocalDate getCreateDate() {
        return createDate;
    }

    /**
     * setCreateDate. Sets the creation date of the division.
     * @param createDate LocalDate
     */
    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    /**
     * getCreateTime. Returns the creation time of the division.
     * @return LocalTime createTime.
     */
    public LocalTime getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime. Sets the creation time of the division.
     * @param createTime LocalTime createTime.
     */
    public void setCreateTime(LocalTime createTime) {
        this.createTime = createTime;
    }

    /**
     * getCreatedBy. Returns who created the division.
     * @return String createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * setCreatedBy. Sets who created the division.
     * @param createdBy String
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * getLastUpdate. Returns the timestamp of the last update of the division.
     * @return Timestamp lastUpdate.
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * setLastUpdate. Sets the time of the last update of the division.
     * @param lastUpdate Timestamp.
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * getLastUpdatedBy. Returns the user who last updated the division.
     * @return lastUpdatedBy String.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * setLastUpdatedBy. Sets the lastUpdateBy field.
     * @param lastUpdateBy String.
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * getCountryID. Returns the country ID associated with the division.
     * @return countryID Integer.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * setCountryID. Sets the country ID of the division.
     * @param countryID Integer
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * toString. Overrides the default toString method. Returns the division name.
     * @return division String
     */
    @Override
    public String toString() {
        return division;
    }
}
