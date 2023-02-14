package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * SEARCH FOR AN ITEM BY ITS NAME.
 */
public class SearchName  extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private String currUser;
    private ArrayList<ListModel> arrLists = new ArrayList<>();
    private EditText query;
    private Button search, goBack;
    private TextView results;
    private Bundle getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name);

        // FIND VIEWS BY IDS
        query = (EditText) findViewById(R.id.query);
        search = (Button) findViewById(R.id.btn_search);
        goBack = (Button) findViewById(R.id.goBack2);
        results = (TextView) findViewById(R.id.results);

        // GET USER DATA
        getData = getIntent().getExtras();
        currUser = getData.getString("username");

        // GET LIST DATA
        ListDatabase ld = new ListDatabase(SearchName.this);
        arrLists = ld.getArrayList(currUser);

        // SET ON CLICK LISTENERS
        search.setOnClickListener(this);
        goBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btn_search:
                results.setText("");
                search();
                break;

            case R.id.goBack2:
                i = new Intent(SearchName.this,UsersPage.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;
        }
    }

    // SEARCH FOR ITEM NAME AND PULL RESULTS
    public void search() {
        String name = query.getText().toString();
        for (int i = 0; i < arrLists.size(); i++) {
            for (int j = 0; j < arrLists.get(i).items.size(); j++) {
                if (arrLists.get(i).items.get(j).name.equals(name)) {
                    results.append("\nList: " + arrLists.get(i).listName +
                            "\nItem Type: " + arrLists.get(i).items.get(j).type +
                            "\nQuantity: " + arrLists.get(i).items.get(j).quantity +
                            "\nChecked Off: " + arrLists.get(i).items.get(j).checkedOff + "\n");
                }
            }
        }
    }
}
