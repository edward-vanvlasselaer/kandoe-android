package be.kdg.kandoe.kandoe.dom;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edward on 11/03/2016.
 */
public class Token {
    @SerializedName("token")
    String token;

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
