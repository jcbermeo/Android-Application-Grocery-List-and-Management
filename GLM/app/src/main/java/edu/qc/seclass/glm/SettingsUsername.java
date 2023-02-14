package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * CHANGE YOUR USERNAME
 */
public class SettingsUsername extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private TextView goBack, submit;
    private EditText emailInput;
    private String currUser;
    private Bundle getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_username);

        // GET VIEWS BY IDS
        goBack = (TextView) findViewById(R.id.goBackText2);
        submit = (TextView) findViewById(R.id.submit);
        emailInput = (EditText) findViewById(R.id.userInput);

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
        switch (v.getId()) {
            case R.id.goBackText2:
                i = new Intent(SettingsUsername.this,Settings.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.submit:
                String username = emailInput.getText().toString();
                if (!username.isEmpty()) {
                    ListDatabase ld = new ListDatabase(SettingsUsername.this);
                    currUser = ld.updateUsername(currUser,username);
                    Toast.makeText(SettingsUsername.this,"Successfully updated username.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SettingsUsername.this,"You must enter an email address.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
