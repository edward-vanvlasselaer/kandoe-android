package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.util.AccountSettings;

public class LandingActivity extends AppCompatActivity {
    private final String TAG = "LandingActivity";
    private Button btnGuest;
    private Button btnRegister;
    private Button btnLogin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        btnGuest = (Button) findViewById(R.id.landing_btn_guest);
        btnRegister = (Button) findViewById(R.id.landing_btn_register);
        btnLogin = (Button) findViewById(R.id.landing_btn_login);

        initListeners();
    }

    private void initListeners() {
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User guest = new User();
                guest.setUsername("guest");
                AccountSettings.setLoggedInUser(guest);
                startActivity(new Intent(v.getContext(), GuestActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });
    }
}
