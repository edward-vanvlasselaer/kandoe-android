package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;


import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private EditText usernameInput;
    private EditText passwordInput;
    private Button btnLogin;

    private View tempview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameInput = (EditText) findViewById(R.id.login_input_username);
        passwordInput = (EditText) findViewById(R.id.login_input_password);
        btnLogin = (Button) findViewById(R.id.login_btn_login);

        initListeners();
    }

    private void initListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempview = v;
                login();
            }
        });
    }

    private void login() {
        Call<Token> call = KandoeApplication.getUserApi().login(usernameInput.getText().toString(), passwordInput.getText().toString());
        call.enqueue(new AbstractExceptionCallback<Token>() {
            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {

                //KandoeApplication.setUserToken(response.body().getToken() == null ? onFailure(); : );
                requestCurrentUser();
            }
        });
    }

    private void requestCurrentUser() {
        Call<User> call = KandoeApplication.getUserApi().getCurrentUser();
        call.enqueue(new AbstractExceptionCallback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                User.setLoggedInUser(response.body());
                Toast.makeText(getBaseContext(), "Hi, " + User.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(tempview.getContext(), MainActivity.class));
            }
        });
    }
}
