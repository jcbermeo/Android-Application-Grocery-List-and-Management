package edu.qc.seclass.glm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;

/**
 *  SEND EMAIL TO USER IF THEY FORGOT PASSWORD
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private static final String EMAIL_SUBJECT = "GroceryListManager: Your Password";
    private TextView goBack;
    private Button submit;
    private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // FIND VIEWS BY IDS
        goBack = (TextView) findViewById(R.id.goBackText2);
        submit = (Button) findViewById(R.id.submit);
        userInput = (EditText) findViewById(R.id.userInput);

        // SET ON CLICK LISTENERS
        goBack.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()) {

            case R.id.goBackText2:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.submit:
                ListDatabase ld = new ListDatabase(this);
                if (ld.accountExists(userInput.getText().toString())) {
                    try {
                        sendEmail();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(this, "That account doesn't exist.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // SEND AN EMAIL TO THE USER WITH MESSAGE CONTAINING PASSWORD
    public void sendEmail() throws JSONException {
        ListDatabase ld = new ListDatabase(ForgotPassword.this);
        String username = userInput.getText().toString();
        String[] email = {ld.getEmail(username)};
        String message = ld.forgotPassword(username);

        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.setData(Uri.parse("mailto:"));
        sendEmail.setType("text/plain");
        sendEmail.putExtra(Intent.EXTRA_EMAIL, email);
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        sendEmail.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(sendEmail, "send"));
            finish();
            Toast.makeText(this,"Successfully sent an email",Toast.LENGTH_SHORT).show();
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this,"Failure: This device cannot send emails.",Toast.LENGTH_SHORT).show();
        }
    }
}
