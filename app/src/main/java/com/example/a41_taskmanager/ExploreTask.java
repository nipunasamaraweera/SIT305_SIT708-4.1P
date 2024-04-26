package com.example.a41_taskmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ExploreTaskActivity extends AppCompatActivity {

    // Declare TextViews to display task details
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dueDateTextView;
    private TextView taskIdTextView;

    // SQLite database helper and database instance
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;

    // Task object to hold the selected task
    private Task task;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_explore_task);

        // Initialize SQLiteHelper and database instance
        dbHelper = new MySQLiteHelper(this);
        database = dbHelper.getWritableDatabase();

        // Initialize TextViews from the layout
        titleTextView = findViewById(R.id.tv_task_title);
        descriptionTextView = findViewById(R.id.tv_task_description);
        dueDateTextView = findViewById(R.id.tv_task_due_date);
        taskIdTextView = findViewById(R.id.tv_task_id);

        // Retrieve the Task object from the intent extras
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");

        // Set TextViews with task details
        if (task != null) {
            taskIdTextView.setText("Viewing Task: " + task.id);
            titleTextView.setText("Title: " + task.title);
            descriptionTextView.setText("Description: " + task.description);
            dueDateTextView.setText("Due Date: " + task.dueDate);
        }

        // Set a click listener for the "Edit" button
        findViewById(R.id.btn_edit_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch EditTask activity with the selected task
                Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
                intent.putExtra("task", task);
                startActivity(intent);
            }
        });

        // Set a click listener for the "Delete" button
        findViewById(R.id.btn_delete_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the task from the database based on its id
                if (task != null) {
                    database.delete("tasks", "id = ?", new String[]{String.valueOf(task.id)});
                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
