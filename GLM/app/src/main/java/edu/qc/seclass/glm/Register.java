package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * CREATE AN ACCOUNT
 */
public class Register extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    EditText fullName, username, email, password;
    Button register;
    TextView goBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // FIND VIEWS BY IDS
        fullName = (EditText) findViewById(R.id.fullNameEditText);
        username = (EditText) findViewById(R.id.userInput);
        email = (EditText) findViewById(R.id.emailAddressEditText);
        password = (EditText) findViewById(R.id.passwordAccountCreateEditText);
        goBack = (TextView) findViewById(R.id.goBackText);
        register = (Button) findViewById(R.id.btn_register);

        // SET ON CLICK LISTENERS
        register.setOnClickListener(this);
        goBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                ListDatabase ld = new ListDatabase(Register.this);
                if (!ld.accountExists(username.getText().toString().trim())) {
                    ld.register(fullName.getText().toString().trim(),
                            username.getText().toString().trim(),
                            email.getText().toString().trim(),
                            password.getText().toString().trim());
                }
                else {
                    Toast.makeText(this,"That account already exists.",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.goBackText:
                startActivity(new Intent(Register.this,MainActivity.class));
        }
    }
}
