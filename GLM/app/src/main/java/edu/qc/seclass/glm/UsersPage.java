package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * USERS PAGE: WHEN YOU FIRST LOG IN
 */
public class UsersPage extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private Button settings, lists, items, searchName, searchType, logout;
    public TextView username;
    public Bundle getData;
    public String currUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(this);
        lists = (Button) findViewById(R.id.lists);
        lists.setOnClickListener(this);
        username = (TextView) findViewById(R.id.displayUsername);
        items = (Button) findViewById(R.id.items);
        items.setOnClickListener(this);
        getData = getIntent().getExtras();
        currUser = getData.getString("username");
        username.setText(currUser);
        searchName = (Button) findViewById(R.id.searchName);
        searchName.setOnClickListener(this);
        searchType = (Button) findViewById(R.id.searchType);
        searchType.setOnClickListener(this);
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()) {
            case R.id.settings:
                i = new Intent(UsersPage.this,Settings.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.lists:
                i = new Intent(UsersPage.this,ListManagement.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.items:
                i = new Intent(UsersPage.this,AddItem.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.searchName:
                i = new Intent(UsersPage.this,SearchName.class);
                i.putExtra("username",currUser);
                startActivity(i);
                break;

            case R.id.searchType:
                i = new Intent(UsersPage.this,SearchType.class);
                i.putExtra("username",currUser);
                startActivity(i);
                break;

            case R.id.logout:
                startActivity(new Intent(UsersPage.this,MainActivity.class));
                break;
        }
    }
}
