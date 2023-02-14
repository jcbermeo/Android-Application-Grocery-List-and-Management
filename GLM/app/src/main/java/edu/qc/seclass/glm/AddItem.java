package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * MODIFY A LIST BY ADDING, DELETING OR CHECKING OFF ITEMS.
 */
public class AddItem extends AppCompatActivity implements View.OnClickListener {

    // PAGE VARIABLES
    private EditText productNameInput, quantities, itemType, listName;
    private Button addButton, deleteButton, checkButton, goBack;
    private String currUser;
    private Bundle getData;
    private int foundIndex, itemIndex;
    private ArrayList<ListModel> arrLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // FIND VIEWS BY IDS
        productNameInput = (EditText) findViewById(R.id.addNameInput);
        quantities = (EditText) findViewById(R.id.quantity);
        addButton = (Button) findViewById(R.id.addItem);
        deleteButton = (Button) findViewById(R.id.deleteItem);
        itemType = (EditText) findViewById(R.id.itemType);
        goBack = (Button) findViewById(R.id.goBack1);
        checkButton = (Button) findViewById(R.id.checkItem);
        itemType = (EditText) findViewById(R.id.itemType);
        listName = (EditText) findViewById(R.id.listName);

        // SET ON CLICK LISTENERS
        addButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        goBack.setOnClickListener(this);
        checkButton.setOnClickListener(this);

        // GET CURRENT USER'S DATA
        getData = getIntent().getExtras();
        currUser = getData.getString("username");

        // GET LIST DATA
        ListDatabase ld = new ListDatabase(AddItem.this);
        arrLists = ld.getArrayList(currUser);
    }

    // ADD AN ITEM TO A LIST
    public void addItem() throws JSONException {

        String productName = productNameInput.getText().toString();
        String q = quantities.getText().toString();
        String type = itemType.getText().toString();
        ListModel list = getList(listName.getText().toString());
        ItemModel item;
        int quantity = 0;

        if (productName.isEmpty() || q.isEmpty() || type.isEmpty() || listName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter all fields.", Toast.LENGTH_SHORT).show();
        }

        else if (!arrLists.contains(list)) {
            Toast.makeText(this, "That list doesn't exist.", Toast.LENGTH_SHORT).show();
        }

        else if (list.contains(productName)){
            quantity = Integer.parseInt(q);
            item = getItem(list,productName,type);
            item.changeQuantity(quantity);
            list.items.set(itemIndex,item);
            arrLists.set(foundIndex,list);
            ListDatabase ld = new ListDatabase(AddItem.this);
            ld.updateLists(currUser,arrLists);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }

        else {
            quantity = Integer.parseInt(q);
            item = new ItemModel(productName,type,quantity);
            list.items.add(item);
            arrLists.set(foundIndex,list);
            ListDatabase ld = new ListDatabase(AddItem.this);
            ld.updateLists(currUser,arrLists);
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
        }
    }

    // DELETE AN ITEM FROM A LIST
    public void deleteItem() throws JSONException {

        String productName = productNameInput.getText().toString();
        String q = quantities.getText().toString();
        String type = itemType.getText().toString();
        ListModel list = getList(listName.getText().toString());
        ItemModel item;

        if (productName.isEmpty() || type.isEmpty() || listName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the item name, type, and list name.", Toast.LENGTH_SHORT).show();
        }

        else if (!arrLists.contains(list)) {
            Toast.makeText(this, "That list doesn't exist.", Toast.LENGTH_SHORT).show();
        }

        else if (list.contains(productName)){
            item = getItem(list,productName,type);
            list.items.remove(item);
            arrLists.set(foundIndex,list);
            ListDatabase ld = new ListDatabase(AddItem.this);
            ld.updateLists(currUser,arrLists);
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "That item doesn't exist.", Toast.LENGTH_SHORT).show();
        }
    }

    // GET A SPECIFIC LIST BY NAME
    public ListModel getList(String listName) {
        ListModel list = new ListModel();
        for (int i = 0; i < arrLists.size(); i++) {
            if (arrLists.get(i).listName.equals(listName)) {
                list = arrLists.get(i);
                foundIndex = i;
                break;
            }
        }
        return list;
    }

    // GET AN ITEM FROM A LIST WITH NAME AND TYPE
    public ItemModel getItem(ListModel list, String itemName, String itemType) {
        ItemModel item = new ItemModel();
        for (int i = 0; i < list.items.size(); i++) {
            if (list.items.get(i).name.equals(itemName) && list.items.get(i).type.equals(itemType)) {
                item = list.items.get(i);
                itemIndex = i;
            }
        }
        return item;
    }

    // CHECK/UNCHECK ANN ITEM IN A LIST
    public void checkItem() throws JSONException {

        String productName = productNameInput.getText().toString();
        String q = quantities.getText().toString();
        String type = itemType.getText().toString();
        ListModel list = getList(listName.getText().toString());
        ItemModel item;

        if (productName.isEmpty() || type.isEmpty() || listName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the item name, type, and list name.", Toast.LENGTH_SHORT).show();
        }

        else if (!arrLists.contains(list)) {
            Toast.makeText(this, "That list doesn't exist.", Toast.LENGTH_SHORT).show();
        }

        else if (list.contains(productName)){
            item = getItem(list,productName,type);
            item.check();
            list.items.set(itemIndex,item);
            arrLists.set(foundIndex,list);
            ListDatabase ld = new ListDatabase(AddItem.this);
            ld.updateLists(currUser,arrLists);
            if (item.checkedOff) {
                Toast.makeText(this, "Item checked", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Item unchecked", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            Toast.makeText(this, "That item doesn't exist.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addItem:
                try {
                    addItem();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.deleteItem:
                try {
                    deleteItem();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.goBack1:
                Intent i = new Intent(AddItem.this,UsersPage.class);
                i.putExtra("username", currUser);
                startActivity(i);
                break;

            case R.id.checkItem:
                try {
                    checkItem();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
