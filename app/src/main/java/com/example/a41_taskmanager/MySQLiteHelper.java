package com.example.a41_taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database name and version constants
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor for the MySQLiteHelper class
    public MySQLiteHelper(Context context) {
        // Call to the superclass constructor to create or open the database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Method called when the database is created for the first time

        // SQL query to create the 'tasks' table with specified columns
        String CREATE_TABLE_QUERY =
                "CREATE TABLE tasks (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT, " +
                        "description TEXT, " +
                        "dueDate TEXT)";

        // Execute the SQL query to create the table in the database
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Method called when the database needs to be upgraded (e.g., version change)

        // Drop the existing 'tasks' table if it exists
        db.execSQL("DROP TABLE IF EXISTS tasks");

        // Recreate the 'tasks' table by calling onCreate method
        onCreate(db);
    }
}