package be.kdg.kandoe.kandoe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        nameInput = (EditText) findViewById(R.id.guest_input_name);
        linkInput = (EditText) findViewById(R.id.guest_input_invitecode);
        btnLogin = (Button) findViewById(R.id.guest_btn_login);
        initListeners();
    }

    private void initListeners() {
        final String guestName= String.valueOf(nameInput.getText());
        if (guestName.equals("")) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<Token> registerCall = KandoeApplication.getUserApi().registerGuest(guestName);
                    registerCall.enqueue(new AbstractExceptionCallback<Token>() {
                        @Override
                        public void onResponse(Response<Token> response, Retrofit retrofit) {
                            Toast.makeText(getBaseContext(), "Hi, " + User.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempview = v;
                //login();
            }
        });
    }
}
