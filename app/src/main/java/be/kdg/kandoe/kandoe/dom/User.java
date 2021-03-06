package be.kdg.kandoe.kandoe.dom;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Edward on 05/03/2016.
 */
public class User implements Serializable {
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
    @SerializedName("password")
    private String password;
    private String imageUrl;
    private boolean playing;

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public String getImageUrl() {
        if (imageUrl == null)
            return "";
        return imageUrl;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setIsPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
