package edu.qc.seclass.glm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * LOGIN SCREEN: WHEN YOU FIRST OPEN APP
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private TextView register, password, login;
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIND VIEWS BY IDS
        register = (TextView) findViewById(R.id.register);
        password = (TextView) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.submit);
        emailInput = (EditText) findViewById(R.id.userInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        // SET ON CLICK LISTENERS
        register.setOnClickListener(this);
        password.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v){
        switch (v.getId()){
            case R.id.register: // go to register page
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.password: // go to forget password page
                startActivity(new Intent(this, ForgotPassword.class));
                break;

            case R.id.submit: // go to User page
                String email = String.valueOf(emailInput.getText());
                String pass = String.valueOf(passwordInput.getText());
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                }
                else {
                    ListDatabase ld = new ListDatabase(MainActivity.this);
                    if (ld.login(email, pass)) {
                        Intent i = new Intent(MainActivity.this, UsersPage.class);
                        i.putExtra("username",email);
                        startActivity(i);
                    }
                }
        }
    }
}