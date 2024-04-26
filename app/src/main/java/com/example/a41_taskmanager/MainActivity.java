package com.example.a41_taskmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declare variables for RecyclerView, task list, buttons, and database
    private RecyclerView recyclerView;
    private List<Task> taskList;
    private ImageView addTaskImageView;
    private SQLiteDatabase database;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_main);

        // Initialize SQLiteHelper and database instance
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        database = dbHelper.getWritableDatabase();

        // Initialize views from the layout
        recyclerView = findViewById(R.id.rv_tasks);
        addTaskImageView = findViewById(R.id.iv_add_task);

        // Set layout manager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the task list and adapter
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);

        // Fetch data from the database and populate the RecyclerView
        fetchDataFromDB();

        // Set a click listener for the "Add Task" button
        addTaskImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the EditTask activity when the button is clicked
                Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
                startActivity(intent);
            }
        });

        // Set a click listener for the "Refresh" button
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh data by fetching from the database again
                fetchDataFromDB();
            }
        });
    }

    // Method to fetch data from the SQLite database
    @SuppressLint("Range")
    private void fetchDataFromDB() {
        // Clear the existing task list
        taskList.clear();

        // Query the "tasks" table in the database, sorted by dueDate in descending order
        Cursor cursor = database.query(
                "tasks",
                null,
                null,
                null,
                null,
                null,
                "dueDate DESC"
        );

        // Populate the task list from the cursor results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                taskList.add(new Task(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("dueDate"))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Set the adapter for the RecyclerView to display the updated task list
        recyclerView.setAdapter(adapter);
    }

    // RecyclerView adapter for displaying tasks
    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        private List<Task> taskList;
        private Context context;

        public TaskAdapter(Context context, List<Task> taskList) {
            this.context = context;
            this.taskList = taskList;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the item layout for each task item in the RecyclerView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            // Bind data to the ViewHolder for each task item
            Task task = taskList.get(position);
            holder.taskTitleTextView.setText(task.title);
        }

        @Override
        public int getItemCount() {
            // Return the size of the task list
            return taskList.size();
        }

        // ViewHolder class for each task item in the RecyclerView
        public class TaskViewHolder extends RecyclerView.ViewHolder {
            public TextView taskTitleTextView;

            public TaskViewHolder(View itemView) {
                super(itemView);
                // Initialize TextView from the item layout
                taskTitleTextView = itemView.findViewById(R.id.tv_task_title);

                // Set click listener to navigate to ExploreTask activity with task details
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Task clickedTask = taskList.get(position);
                            Intent intent = new Intent(context, ExploreTaskActivity.class);
                            intent.putExtra("task", clickedTask);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
