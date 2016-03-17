package be.kdg.kandoe.kandoe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.kdg.kandoe.kandoe.R;

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempview = v;
                //login();
            }
        });
    }
}
