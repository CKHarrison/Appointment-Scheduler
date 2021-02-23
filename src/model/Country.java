package model;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/** Country model Class. This class is a POJO representation of the Country table in the database. */
public class Country {
    private int countryID;
    private String country;
    private LocalDate createDate;
    private LocalTime createTime;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    /**
     * Country constructor. Instantiates Country instance.
     * @param countryID Integer
     * @param country String
     * @param createDate LocalDate
     * @param createTime LocalTime
     * @param lastUpdate Timestamp
     * @param lastUpdateBy lastUpdateBy
     */
    public Country(int countryID, String country, LocalDate createDate, LocalTime createTime, Timestamp lastUpdate, String lastUpdateBy) {
        this.countryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createTime = createTime;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * getCountryID. Returns the ID of the country.
     * @return countryID Integer.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * setCountryID. Sets the ID of the country.
     * @param countryID Integer
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * getCountry. Returns the associated country name.
     * @return country String.
     */
    public String getCountry() {
        return country;
    }

    /**
     * setCountry. Sets the country name.
     * @param country String
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getCreateDate. Returns the create Date of the country.
     * @return LocalDate createDate.
     */
    public LocalDate getCreateDate() {
        return createDate;
    }

    /**
     * setCreateDate. Sets the create date of the country.
     * @param createDate LocalDate
     */
    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    /**
     * getCreateTime. Returns the create time of the country.
     * @return LocalTime createTime.
     */
    public LocalTime getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime. Sets create time of the country.
     * @param createTime LocalTime
     */
    public void setCreateTime(LocalTime createTime) {
        this.createTime = createTime;
    }

    /**
     * getLastUpdate. Returns the last update of the country.
     * @return lastUpdate Timestamp.
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * setLastUpdate. Sets the lastUpdate of the country.
     * @param lastUpdate Timestamp.
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * getLastUpdateBy. Returns the user who last updated the country.
     * @return lastUpdate String.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * setLastUpdate. Sets the user who last updated the country.
     * @param lastUpdateBy String
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * toString(). Override the default toString method to return the country name.
     * @return country String.
     */
    @Override
    public String toString() {
        return country;
    }
}
