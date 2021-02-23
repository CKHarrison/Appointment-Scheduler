package model;
/** Contact model class.  Will act as POJO representation of the Contact table from the database. */
public class Contact {
    private int contactID;
    private String contactName;
    private String email;

    /**
     * Contact constructor. Instantiates instance object of Contact class.
     * @param contactID Integer
     * @param contactName String
     * @param email String
     */
    public Contact(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * getContactID. Returns ID of contact.
     * @return contactID Integer.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * setContactID. Sets ID of contact
     * @param contactID Integer
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * getContactName. Returns the name of the contact.
     * @return contactName String
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * setContactName. Sets the name of the contact.
     * @param contactName String
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * getEmail. Returns the email of the contact.
     * @return email String
     */
    public String getEmail() {
        return email;
    }

    /**
     * setEmail. Sets email of contact.
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * toString(). Overrides the default toString() method. Returns the contact name instead.
     * @return contactName String
     */
    @Override
    public String toString() {
        return this.getContactName();
    }
}
