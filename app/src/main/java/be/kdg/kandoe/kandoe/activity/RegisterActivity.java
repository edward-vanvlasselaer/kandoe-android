package be.kdg.kandoe.kandoe.activity;

import android.app.ProgressDialog;
import android.content.Context;
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
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";

    private EditText usernameInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button btnRegister;

    private ProgressDialog dialog;
    private Context mContext;
    private Thread mThread;

    private View tempview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;

        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setMessage("Verifying credentials..");

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
                dialog.show();
                final User userRegister = new User();
                userRegister.setFirstName(firstNameInput.getText().toString());
                userRegister.setLastName(lastNameInput.getText().toString());
                userRegister.setUsername(usernameInput.getText().toString());
                userRegister.setEmail(usernameInput.getText().toString());
                userRegister.setPassword(passwordInput.getText().toString());

                mThread = new Thread() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            register(userRegister);
                        }
                    }
                };
                mThread.start();

            }
        });
    }
    private void register(User userRegister){
        Call<Token> call = KandoeApplication.getUserApi().registerUser(userRegister);
        call.enqueue(new AbstractExceptionCallback<Token>() {
            @Override
            public void onFailure(Throwable t) {
                killDialog();
            }

            @Override //REGISTER
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                KandoeApplication.setUserToken(response.body().getToken());

                Call<User> call = KandoeApplication.getUserApi().getCurrentUser();
                call.enqueue(new AbstractExceptionCallback<User>() {
                    @Override //LOGIN
                    public void onResponse(Response<User> response, Retrofit retrofit) {
                        AccountSettings.setLoggedInUser(response.body());
                        Toast.makeText(getBaseContext(), "Hi, " + AccountSettings.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                        Intent myintent = new Intent(RegisterActivity.this, OrganisationActivity.class);
                        RegisterActivity.this.startActivity(myintent);
                        finish();
                        killDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        killDialog();
                    }
                });

            }
        });
    }
    private void killDialog(){
        if(dialog!=null)
            dialog.hide();
        dialog = null;
    }
}
