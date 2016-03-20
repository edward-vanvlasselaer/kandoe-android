package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;


public class GuestActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText linkInput;
    private Button btnLogin;

    private boolean noCircle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        nameInput = (EditText) findViewById(R.id.guest_input_name);
        linkInput = (EditText) findViewById(R.id.guest_input_invitecode);
        btnLogin = (Button) findViewById(R.id.guest_btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!noCircle) {
                    User guest = new User();
                    guest.setFirstName(nameInput.getText().toString());

                    Call<Token> registerCall = KandoeApplication.getUserApi().registerGuest(guest);
                    registerCall.enqueue(new AbstractExceptionCallback<Token>() {
                        @Override //REGISTER
                        public void onResponse(Response<Token> response, Retrofit retrofit) {
                            KandoeApplication.setUserToken(response.body().getToken());
                            Call<User> call = KandoeApplication.getUserApi().getCurrentUser();
                            call.enqueue(new AbstractExceptionCallback<User>() {
                                @Override //LOGIN
                                public void onResponse(Response<User> response, Retrofit retrofit) {
                                    AccountSettings.setLoggedInUser(response.body());
                                    callCircle();
                                }
                            });
                        }
                    });
                } else {
                    callCircle();
                }
            }
        });
    }

    private void callCircle() {
        Call<Theme> circleCall = KandoeApplication.getCircleApi().joinCircleByLink(linkInput.getText().toString());
        circleCall.enqueue(new AbstractExceptionCallback<Theme>() {
            @Override
            public void onResponse(Response<Theme> response, Retrofit retrofit) {
                if (response.body() == null) {
                    noCircle = true;
                    Toast.makeText(getApplicationContext(), "Invalid invite link", Toast.LENGTH_SHORT).show();
                } else {
                    ThemeCardActivity.setCurrentTheme(response.body());
                    Toast.makeText(getBaseContext(), "Hi, " + AccountSettings.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(GuestActivity.this, ThemeCardActivity.class);
                    GuestActivity.this.startActivity(myintent);
                }
            }
        });
    }

}
