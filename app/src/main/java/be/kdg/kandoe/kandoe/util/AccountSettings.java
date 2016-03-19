package be.kdg.kandoe.kandoe.util;

import android.content.Intent;
import android.widget.Toast;

import be.kdg.kandoe.kandoe.activity.OrganisationActivity;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.exception.UserException;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

public class AccountSettings {
    private static User loggedInUser;

    public static synchronized User getLoggedInUser() {
        if (loggedInUser == null) {
            Call<User> call = KandoeApplication.getUserApi().getCurrentUser();
            call.enqueue(new AbstractExceptionCallback<User>() {
                @Override
                public void onResponse(Response<User> response, Retrofit retrofit) {
                    if (response.body() == null)
                        throw new RuntimeException("Loginactivity: response.body() IS NULL");
                    AccountSettings.setLoggedInUser(response.body());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new UserException("No user logged in" + t.getMessage());
                }
            });

        }
        while (loggedInUser == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new UserException("something went wrong");
            }
        }
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        AccountSettings.loggedInUser = loggedInUser;
    }
}
