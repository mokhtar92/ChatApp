package entity;

import java.io.Serializable;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 *
 * @author Ghada
 */
public class User implements Serializable {

    private Long recId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String country;
    private Date birthDate;
    private String gender;
    private String myStatus;
    private Boolean active;
    private String imgURL;

    public String getImgURL() {
        return imgURL;
    }

    public boolean setImgURL(String imgURL) {
        if (imgURL != null) {
            this.imgURL = imgURL;
            return true;
        }
        return false;
    }

    public User() {
    }

    public User(String username, String email, String fname, String lname, String password, String gender, String country) {

        this.email = email;
        this.firstName = fname;
        this.lastName = lname;
        this.password = password;
        this.gender = gender;
        this.country = country;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username, String fname, String lname, String gender, String country) {

        this.firstName = fname;
        this.lastName = lname;
        this.gender = gender;
        this.country = country;

    }

    public User(String username, String email, String fname, String lname, String password, String gender, String country, String status) {

        this.email = email;
        this.firstName = fname;
        this.lastName = lname;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.myStatus = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean setFirstName(String firstName) {
        if (Pattern.compile("^[\\p{L} .'-]+$").matcher(firstName).matches()) {
            this.firstName = firstName;
            return true;
        }
        return false;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean setLastName(String lastName) {
        if (Pattern.compile("^[\\p{L} .'-]+$").matcher(lastName).matches()) {
            this.lastName = lastName;
            return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        if (Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches()) {
            this.email = email;
            return true;
        }
        return false;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {

        if (password.length() >= 5) {
            this.password = password;
            return true;
        }
        return false;
    }

    public String getCountry() {
        return country;
    }

    public boolean setCountry(String country) {
        if (Pattern.compile("^[\\p{L} .'-]+$").matcher(country).matches()) {
            this.country = country;
            return true;
        }
        return false;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public boolean setBirthDate(Date birthDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (birthDate.before(sdf.parse("2003-00-00 00:00:00"))) {
                this.birthDate = birthDate;
            }
            return true;
        } catch (ParseException ex) {
            ex.printStackTrace(System.out);
        }
        return false;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(String myStatus) {
        this.myStatus = myStatus;
    }

}
