package be.kdg.kandoe.kandoe.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private EditText usernameInput;
    private EditText passwordInput;
    private Button btnLogin;


    private View tempview;
    private ProgressDialog dialog;
    private Context mContext;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;

        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setMessage("Verifying credentials..");

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
                dialog.show();
                tempview = v;
                mThread = new Thread() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            login();
                        }
                    }
                };
                mThread.start();
            }
        });
    }

    private void login() {
        Call<Token> call = KandoeApplication.getUserApi().login(usernameInput.getText().toString(), passwordInput.getText().toString());
        call.enqueue(new AbstractExceptionCallback<Token>() {
            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getToken() != null) {
                    KandoeApplication.setUserToken(response.body().getToken());
                    requestCurrentUser();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.hide();
                dialog = null;
                if (t.getMessage().contains("dolha.in")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Uh oh :(")
                            .setMessage("Looks like something went wrong,\nmake sure you are connected to the internet.")
                            .setNeutralButton("Will do!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                Log.e("","RETROFIT FAILED: " + t.getMessage());
            }
        });
    }

    private void requestCurrentUser() {
        dialog.setMessage("Logging in..");
        Call<User> call = KandoeApplication.getUserApi().getCurrentUser();
        call.enqueue(new AbstractExceptionCallback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.body() == null)
                    throw new RuntimeException("Loginactivity: response.body() IS NULL");
                AccountSettings.setLoggedInUser(response.body());
                Toast.makeText(getBaseContext(), "Hi, " + AccountSettings.getLoggedInUser().getFirstName() + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(tempview.getContext(), OrganisationActivity.class));
                dialog.hide();
                dialog = null;
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.hide();
                dialog = null;
                if (t.getMessage().contains("dolha.in")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Uh oh :(")
                            .setMessage("Looks like something went wrong,\nmake sure you are connected to the internet.")
                            .setNeutralButton("Will do!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                Log.e("", "RETROFIT FAILED: " + t.getMessage());
            }
        });
    }
}