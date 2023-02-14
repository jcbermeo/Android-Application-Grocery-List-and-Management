package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * GROCERY LIST MANAGER DATABASE
 * REFER TO THIS CLASS TO GET INFORMATION ON USERS AND THEIR LISTS/ITEMS.
 */
public class ListDatabase extends SQLiteOpenHelper {

    // DATABASE INFORMATION
    private Context context;
    private static final String DATABASE_NAME = "ListManager.db";
    private static final int DATABASE_VERSION = 6;

    // TABLE NAME
    private static final String TABLE_NAME = "List_Table";

    // TABLE COLUMNS
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LISTS = "lists";

    // CONSTRUCTOR
    public ListDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // LOAD DATABASE ON CREATION
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                USERNAME + " TEXT, " +
                EMAIL + " TEXT, " +
                PASSWORD + " TEXT, " +
                LISTS + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    // RESET DATABASE IF VERSION CHANGES
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // CREATE AN ACCOUNT
    public void register(String name, String username, String email, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        ArrayList<ListModel> arrLists = new ArrayList<ListModel>();
        String json = new Gson().toJson(arrLists);

        cv.put(NAME,name);
        cv.put(USERNAME, username);
        cv.put(EMAIL, email);
        cv.put(PASSWORD, password);
        cv.put(LISTS, json);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully registered", Toast.LENGTH_SHORT).show();
        }
    }

    // LOG INTO YOUR ACCOUNT
    public boolean login(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String userQuery = "SELECT " + USERNAME + " FROM " + TABLE_NAME + " WHERE " + USERNAME + " =?";
        Cursor userCursor = sqLiteDatabase.rawQuery(userQuery, new String[]{username});
        if (userCursor.getCount() <= 0) {
            userCursor.close();
            Toast.makeText(context, "That account doesn't exist.", Toast.LENGTH_SHORT).show();
            return false;
        }
        userCursor.close();
        String passwordQuery = "SELECT " +PASSWORD + " FROM " + TABLE_NAME + " WHERE " + PASSWORD + " =?";
        Cursor passwordCursor = sqLiteDatabase.rawQuery(passwordQuery, new String[]{password});
        if (passwordCursor.getCount() <= 0) {
            passwordCursor.close();
            Toast.makeText(context, "Incorrect password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        passwordCursor.close();
        return true;
    }

    // CHANGE YOUR USERNAME
    public String updateUsername(String oldUsername, String newUsername) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, newUsername);
        sqLiteDatabase.update(TABLE_NAME,values,USERNAME + "=?", new String[]{oldUsername});
        return newUsername;
    }

    // CHANGE YOUR PASSWORD
    public String updatePassword(String username, String newPassword) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD, newPassword);
        sqLiteDatabase.update(TABLE_NAME,values,USERNAME+ "=?", new String[]{username});
        return username;
    }

    // SAVE CHANGES TO LISTS
    public void updateLists(String username, ArrayList<ListModel> arrLists) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String json = new Gson().toJson(arrLists);
        ContentValues values = new ContentValues();
        values.put(LISTS, json);
        sqLiteDatabase.update(TABLE_NAME,values,USERNAME + "=?", new String[]{username});
    }

    // GET LISTS FROM DATABASE
    public ArrayList<ListModel> getArrayList(String username) {
        int index = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT *  FROM " + TABLE_NAME + " WHERE " + USERNAME + " =?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{username});
        if (cursor.moveToFirst()) {
            index = cursor.getColumnIndex(LISTS);
        }
        String json = cursor.getString(index);
        cursor.close();
        Type arrType = new TypeToken<ArrayList<ListModel>>(){}.getType();
        return new Gson().fromJson(json, arrType);
    }

    // GET EMAIL ADDRESS (USED FOR FORGOT PASSWORD)
    public String getEmail(String username) {
        int index = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " =?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username});
        if (cursor.moveToFirst()) {
            index = cursor.getColumnIndex(EMAIL);
        }
        String email = cursor.getString(index);
        cursor.close();
        return email;
    }

    // GET THE MESSAGE WITH PASSWORD TO SEND TO USER WHO FORGETS PASSWORD
    public String forgotPassword(String username) {
        int passIndex = 0;
        int emailIndex = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " =?";

        Cursor passCursor = sqLiteDatabase.rawQuery(query, new String[]{username});
        if (passCursor.moveToFirst()) {
            passIndex= passCursor.getColumnIndex(PASSWORD);
        }
        String password = passCursor.getString(passIndex);
        passCursor.close();

        Cursor emailCursor = sqLiteDatabase.rawQuery(query, new String[]{username});
        if (emailCursor.moveToFirst()) {
            emailIndex = emailCursor.getColumnIndex(EMAIL);
        }
        String email = emailCursor.getString(emailIndex);
        emailCursor.close();

        String message = "Forgot your password? No worries, it happens to the best of us." +
                "\n Your password is " + password + "." +
                "\n\nSincerely, \nThe GroceryListManager Team";
       return message;
    }

    // CHECK IF AN ACCOUNT EXISTS GIVEN A USERNAME
    public boolean accountExists(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " =?";
        int index = -1;
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
