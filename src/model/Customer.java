package model;


import utilities.TimeConversion;

import java.time.LocalDateTime;

/** Customer model class. Customer POJO representation of the Customer table in the database */
public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDateTime;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;

    /**
     * Constructor for Customer Class
     * @param customerID Integer
     * @param customerName String
     * @param address String
     * @param postalCode String
     * @param phone String
     * @param createDateTime LocalDateTime
     * @param createdBy String
     * @param lastUpdate LocalDateTime
     * @param lastUpdatedBy String
     * @param divisionID Integer
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phone, LocalDateTime createDateTime, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDateTime = createDateTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }

    /**
     * getCustomerID. Returns the ID of the customer.
     * @return customerID Integer.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * setCustomerID. Sets the ID of the customer.
     * @param customerID Integer.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * getCustomerName. Returns the customer's name.
     * @return customerName String.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * setCustomerName. Sets the name of the customer
     * @param customerName String
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * getAddress. Returns the address of the customer.
     * @return address String.
     */
    public String getAddress() {
        return address;
    }

    /**
     * setAddress. Sets the address of the customer.
     * @param address String.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getPostalCode. Returns the postalCode of the customer.
     * @return postalCode String
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * setPostalCode. Sets the customer postalCode.
     * @param postalCode String
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * getPhone. Returns the phone number of the customer.
     * @return phone String.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * setPhone. Returns the phone number of the customer.
     * @param phone String
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * getCreateDateTime. Returns the creation date and time of the customer.
     * @return LocalDateTime createDateTime.
     */
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    /**
     * getFormattedCreateDate. Returns a formatted string representation of the creation date.
     * @return createDate String
     */
    public String getFormattedCreateDate() {
        return TimeConversion.formatDate(getCreateDateTime());
    }

    /**
     * setCreateDateTime. sets the creation date and time of the customer.
     * @param createDateTime LocalDateTime
     */
    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * getCreatedBy. Returns the user that created the appointment.
     * @return createdBy String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * setCreatedBy. Sets the user who created the customer.
     * @param createdBy String
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * getLastUpdate. Returns the Local date and time of the last time the customer was updated.
     * @return LocalDateTime getLastUpdate.
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * getFormattedLastUpdate. Returns a formatted string of the last time the customer was updated.
     * @return String LastUpdate
     */
    public String getFormattedLastUpdate() {
        return TimeConversion.formatDate(getLastUpdate());
    }

    /**
     * setLastUpdate. Sets the Local Date and Time of the last customer's last update.
     * @param lastUpdate LocalDateTime.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * getLastUpdatedBy. Returns a string of the user who last updated the customer.
     * @return lastUpdatedBy String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * setLastUpdatedBy. Sets the lastUpdatedBy field of the customer.
     * @param lastUpdatedBy String.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * getDivisionID. Returns the divisionID associated with the customer.
     * @return getDivisionID Integer.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * setDivisionID. Sets the division ID of the division associated with the customer.
     * @param divisionID Integer.
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * toString(). Overrides the default toString method to return the customer ID and Name.
     * @return String
     */
    @Override
    public String toString() {
        return customerID +": " + customerName;
    }
}
