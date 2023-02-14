package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * CHANGE YOUR PASSWORD
 */
public class SettingsPassword extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private TextView goBack, submit;
    private EditText passwordEntered;
    private String currUser;
    private Bundle getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_password);

        // GET VIEWS BY IDS
        goBack = (TextView) findViewById(R.id.goBackText2);
        submit = (TextView) findViewById(R.id.submit);
        passwordEntered = (EditText) findViewById(R.id.passwordEntered);

        // SET ON CLICK LISTENERS
        goBack.setOnClickListener(this);
        submit.setOnClickListener(this);

        // GET CURRENT USER'S DATA
        getData = getIntent().getExtras();
        currUser = getData.getString("username");
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()) {
            case R.id.goBackText2:
                i = new Intent(SettingsPassword.this,Settings.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.submit:
                String password = String.valueOf(passwordEntered.getText());
                if (!password.isEmpty()) {
                    ListDatabase ld = new ListDatabase(SettingsPassword.this);
                    currUser = ld.updatePassword(currUser,password);
                    Toast.makeText(SettingsPassword.this,"Successfully updated password.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SettingsPassword.this,"You must enter a password.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
