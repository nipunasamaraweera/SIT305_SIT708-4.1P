package com.example.a41_taskmanager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private TextView headingTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private SQLiteDatabase database;
    private Task task;
    private String dueDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_edit_task);

        // Initialize SQLiteHelper and database instance
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        database = dbHelper.getWritableDatabase();

        // Initialize views from the layout
        headingTextView = findViewById(R.id.tv_heading);
        titleEditText = findViewById(R.id.et_title);
        descriptionEditText = findViewById(R.id.et_description);

        // Retrieve the Task object from the intent extras
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");

        // If editing an existing task, populate the UI with its details
        if (task != null) {
            headingTextView.setText("Editing the Task: " + task.id);
            titleEditText.setText(task.title);
            descriptionEditText.setText(task.description);
            dueDate = task.dueDate;

            // Update the button text to indicate an update operation
            Button createButton = findViewById(R.id.btn_create_task);
            createButton.setText("Update");
        }

        // Set a click listener for the "Add Task" button
        findViewById(R.id.btn_create_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to add or update task data in the database
                addOrUpdateTask();
            }
        });

        // Set a click listener for the "Pick Date" button
        findViewById(R.id.btn_pick_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a DatePickerDialog to select a due date for the task
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set the selected date in the desired format
                                dueDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    // Method to add or update task data in the SQLite database
    private void addOrUpdateTask() {
        // Retrieve text values from title and description EditText fields
        String taskTitle = titleEditText.getText().toString();
        String taskDescription = descriptionEditText.getText().toString();

        // Check if any of the required fields are empty
        if (taskTitle.isEmpty() || taskDescription.isEmpty() || dueDate.isEmpty()) {
            // Display a toast message indicating that all fields are required
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create ContentValues to hold task data
        ContentValues values = new ContentValues();
        values.put("title", taskTitle);
        values.put("description", taskDescription);
        values.put("dueDate", dueDate);

        // Check if the task object is null (indicating a new task creation)
        if (task == null) {
            // Insert new task data into the "tasks" table
            database.insert("tasks", null, values);
            Toast.makeText(this, "Task created", Toast.LENGTH_SHORT).show();
        } else {
            // Update existing task data in the "tasks" table based on task id
            database.update("tasks", values, "id = ?", new String[]{String.valueOf(task.id)});
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }

        // Finish the activity (return to the previous screen)
        finish();
    }
}
