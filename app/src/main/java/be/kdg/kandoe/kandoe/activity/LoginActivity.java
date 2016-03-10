package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.ExceptionHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        Callback<String> c = new Callback<String>() {
            @Override
            public void success(String t, Response response) {
                String token = t; //todo iets doen met de token?
                requestCurrentUser();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleFailure(retrofitError);
            }
        };

        KandoeApplication.getUserApi().login(
                usernameInput.getText().toString(),
                passwordInput.getText().toString(),
                c);
    }

    private void requestCurrentUser() {
        KandoeApplication.getUserApi().getCurrentUser(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                User.setLoggedInUser(user);
                Toast.makeText(getBaseContext(), "Hi, " + User.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(tempview.getContext(), MainActivity.class));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleFailure(retrofitError);
            }
        });
    }

    public void handleFailure(RetrofitError retrofitError) {
        ExceptionHelper.showRetrofitError(retrofitError, getBaseContext(), TAG);
    }
}
