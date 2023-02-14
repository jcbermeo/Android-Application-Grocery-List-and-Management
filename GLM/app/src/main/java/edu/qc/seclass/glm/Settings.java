package edu.qc.seclass.glm;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * CHANGE YOUR USERNAME OR PASSWORD
 */
public class Settings extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private Button email, password;
    private TextView goBack;
    private String currUser;
    private Bundle getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // GET VIEWS BY IDS
        email = (Button) findViewById(R.id.btn_register);
        password = (Button) findViewById(R.id.changePassword);
        goBack = (TextView) findViewById(R.id.goBackText);

        // SET ON CLICK LISTENERS
        email.setOnClickListener(this);
        password.setOnClickListener(this);
        goBack.setOnClickListener(this);

        // GET CURRENT USER DATA
        getData = getIntent().getExtras();
        currUser = getData.getString("username");
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btn_register:
                i = new Intent(Settings.this, SettingsUsername.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;


            case R.id.changePassword:
                i = new Intent(Settings.this,SettingsPassword.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.goBackText:
                i = new Intent(Settings.this,UsersPage.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;
       }
    }
}
