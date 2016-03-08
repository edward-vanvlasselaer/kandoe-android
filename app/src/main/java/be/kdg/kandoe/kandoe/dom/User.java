package be.kdg.kandoe.kandoe.dom;

import com.google.gson.annotations.SerializedName;

import be.kdg.kandoe.kandoe.exception.UserException;

/**
 * Created by Edward on 05/03/2016.
 */
public class User {
    private static User loggedInUser = null;

    @SerializedName("userId")
    private Integer userId;
    @SerializedName("username")
    private String username;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("email")
    private String email;

    public User() {
    }

    public static User getLoggedInUser(){
        if (loggedInUser == null){
            throw new UserException("No user logged in");
        }
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        User.loggedInUser = loggedInUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
