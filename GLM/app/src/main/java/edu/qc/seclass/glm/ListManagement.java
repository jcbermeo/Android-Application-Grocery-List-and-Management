package edu.qc.seclass.glm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * ADD, DELETE, RENAME, VIEW LISTS
 */
public class ListManagement  extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private Button goBack, btn_add_list, addCancel, addCreate;
    private EditText addListName;
    private String currUser;
    private Bundle getData;
    private ArrayList<ListModel> arrLists = new ArrayList<>();
    private ArrayList<ListModel> gottenArray = new ArrayList<>();
    private AlertDialog.Builder  builder_addList, builder_deleteList, builder_renameList;
    private AlertDialog dialog_addList, dialog_deleteList, dialog_renameList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmanagement);

        // GET CURRENT USER DATA
        getData = getIntent().getExtras();
        currUser = getData.getString("username");
        ListDatabase ld = new ListDatabase(ListManagement.this);
        gottenArray = ld.getArrayList(currUser);

        // SET UP RECYCLER VIEW
        recyclerView = findViewById(R.id.recycler_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this,arrLists,currUser);
        recyclerView.setAdapter(adapter);

        // FIND VIEWS BY IDS
        goBack = (Button) findViewById(R.id.goBack);
        btn_add_list = (Button) findViewById(R.id.btn_add_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);

        // SET ON CLICK LISTENERS
        goBack.setOnClickListener(this);
        btn_add_list.setOnClickListener(this);

        // DISPLAY LISTS ON SCREEN
        loadLists();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.goBack:
                Intent i = new Intent(ListManagement.this,UsersPage.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.btn_add_list:
                popWin2AddAList(); // add a list in list management
                break;

            case R.id.btn_create_to_add:
                dialog_addList.dismiss();
                try {
                    createList(); // create a list
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_cancel_to_add:
                dialog_addList.dismiss();
                break;

            case R.id.btn_cancel_to_delete:
                dialog_deleteList.dismiss();
                break;

            case R.id.btn_delete_the_list:
                dialog_deleteList.dismiss();
                break;

            case R.id.btn_cancel_to_rename:
                dialog_renameList.dismiss();
                break;

            case R.id.btn_rename_the_list:
                dialog_renameList.dismiss();
                break;

            case R.id.btn_view_items:
                break;

        }
    }

    // DISPLAY AN ADDED LIST ON THE SCREEN
    public void popWin2AddAList(){
        builder_addList = new AlertDialog.Builder(this);
        View pop = getLayoutInflater().inflate(R.layout.list_management_add_list,null);
        // declare
        addCancel = (Button) pop.findViewById(R.id.btn_cancel_to_add);
        addCreate = (Button) pop.findViewById(R.id.btn_create_to_add);
        addListName = (EditText) pop.findViewById(R.id.et_list_name);
        // OnClick
        addCancel.setOnClickListener(this);
        addCreate.setOnClickListener(this);

        builder_addList.setView(pop);
        dialog_addList = builder_addList.create();
        dialog_addList.show();
    }

    // CREATE A NEW LIST
    public void createList() throws JSONException {
        String name = "";
        if (!addListName.getText().toString().equals("")) {
            name = addListName.getText().toString();

            if (listExists(name)) {
                Toast.makeText(ListManagement.this,"That list already exists.",Toast.LENGTH_SHORT).show();
            }

            else {
                arrLists.add(new ListModel(name));
                adapter.notifyItemInserted(arrLists.size() - 1);
                recyclerView.scrollToPosition(arrLists.size() - 1);
                ListDatabase ld = new ListDatabase(ListManagement.this);
                ld.updateLists(currUser, arrLists);
            }
        }

        else {
            Toast.makeText(ListManagement.this,"Enter a name please", Toast.LENGTH_SHORT).show();
        }
    }

    // DISPLAY LISTS ON SCREEN
    public void loadLists() {
        for (int i = 0; i < gottenArray.size(); i++) {
            String name = gottenArray.get(i).listName;
            ArrayList<ItemModel> items = gottenArray.get(i).items;
            arrLists.add(new ListModel(name,items));
            System.out.println("LOAD ITEM:" + arrLists.get(i).items);
            adapter.notifyItemInserted(arrLists.size()-1);
            recyclerView.scrollToPosition(arrLists.size()-1);
        }
    }

    // CHECK IF A LIST EXISTS OR NOT
    public boolean listExists(String listName) {
        for (int i = 0; i < gottenArray.size(); i++) {
            if (gottenArray.get(i).listName.equals(listName)) {
                return true;
            }
        }
        return false;
    }
}
