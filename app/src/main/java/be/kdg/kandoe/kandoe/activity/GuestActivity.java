package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.CustomPagerAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by claudiu on 17/03/16.
 */
public class GuestActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText linkInput;
    private Button btnLogin;

    private View tempview;
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
                //Toast.makeText(getApplicationContext(), "YES!", Toast.LENGTH_SHORT).show();
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
                                    // Toast.makeText(getBaseContext(), "Hi, " + AccountSettings.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                                    // Intent myintent = new Intent(GuestActivity.this, MainActivity.class);
                                    // GuestActivity.this.startActivity(myintent);
                                    callCircle();
                                    //finish();

                                }
                            });
                        }
                    });
                } else {
                    callCircle();

                }


            }
        });

        //initListeners();
    }

    private void callCircle() {
        Call<Theme> circleCall = KandoeApplication.getCircleApi().joinCircleByLink(linkInput.getText().toString());
        circleCall.enqueue(new AbstractExceptionCallback<Theme>() {
            @Override
            public void onResponse(Response<Theme> response, Retrofit retrofit) {
                if (response == null) {
                    noCircle = true;
                } else {
                    ThemeCardActivity.setCurrentTheme(response.body());
                    Intent myintent = new Intent(GuestActivity.this, MainActivity.class);
                    GuestActivity.this.startActivity(myintent);
//                    Toast.makeText(getBaseContext(), response.body().getThemeId(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void initListeners() {
//        final String guestName = String.valueOf(nameInput.getText());
//        if (!guestName.equals("")) {
//            btnLogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Call<Token> registerCall = KandoeApplication.getUserApi().registerGuest(guestName);
//                    registerCall.enqueue(new AbstractExceptionCallback<Token>() {
//                        @Override
//                        public void onResponse(Response<Token> response, Retrofit retrofit) {
//                            Toast.makeText(getBaseContext(), "Hi, " + AccountSettings.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
//
//                    User user=new User();
//                    user.setFirstName(guestName);
//                    Call<Token> registerCall = KandoeApplication.getUserApi().registerGuest(user);
//                   registerCall.enqueue(new Callback<Token>() {
//                       @Override
//                       public void onResponse(Response<Token> response, Retrofit retrofit) {
//                           Toast.makeText(getApplicationContext(), "YES!", Toast.LENGTH_SHORT).show();
//
//                       }
//
//                       @Override
//                       public void onFailure(Throwable t) {
//                           Toast.makeText(getApplicationContext(), "NO!", Toast.LENGTH_SHORT).show();
//
//                       }
//                   });
//                }
//            });
//        }
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tempview = v;
//                //login();
//            }
//        });
//    }
}
