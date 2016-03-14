package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.User;
import retrofit.Callback;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";

    private EditText usernameInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button btnRegister;

    private View tempview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = (EditText) findViewById(R.id.register_input_username);
        firstNameInput = (EditText) findViewById(R.id.register_input_firstname);
        lastNameInput = (EditText) findViewById(R.id.register_input_lastname);
        emailInput = (EditText) findViewById(R.id.register_input_email);
        passwordInput = (EditText) findViewById(R.id.register_input_password);
        confirmPasswordInput = (EditText) findViewById(R.id.register_input_confirm);
        btnRegister = (Button) findViewById(R.id.register_btn_register);

        initListeners();
    }

    private void initListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userRegister = new User();
                userRegister.setFirstName(firstNameInput.getText().toString());
                userRegister.setLastName(lastNameInput.getText().toString());
                userRegister.setUsername(usernameInput.getText().toString());
                userRegister.setEmail(usernameInput.getText().toString());
                userRegister.setPassword(passwordInput.getText().toString());

                /*KandoeApplication.getUserApi().registerUser(userRegister, new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        KandoeApplication.getUserApi().getCurrentUser(new Callback<User>() {
                            @Override
                            public void success(User user, Response response) {
                                User.setLoggedInUser(user);
                                Toast.makeText(getBaseContext(), "Hi, " + User.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                                Intent myintent = new Intent(RegisterActivity.this,MainActivity.class);
                                RegisterActivity.this.startActivity(myintent);
                                finish();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                ExceptionHelper.showRetrofitError(retrofitError,getApplicationContext(),TAG);
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        ExceptionHelper.showRetrofitError(retrofitError,getApplicationContext(),TAG);
                    }
                }); //if register succesfull, login directly*/
            }
        });
    }


}
