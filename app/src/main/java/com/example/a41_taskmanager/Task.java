package com.example.a41_taskmanager;

import java.io.Serializable;

// Task class implementing Serializable interface for object serialization
public class Task implements Serializable {

    // Instance variables to store task details
    private long id;             // Unique identifier for the task
    private String title;        // Title or name of the task
    private String description;  // Description or details of the task
    private String dueDate;      // Due date of the task

    // Constructor to initialize Task object with provided attributes
    public Task(long id, String title, String description, String dueDate) {
        this.id = id;                    // Initialize task ID
        this.title = title;              // Initialize task title
        this.description = description;  // Initialize task description
        this.dueDate = dueDate;          // Initialize task due date
    }

    // Getter methods for task attributes
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    // Setter methods for task attributes (optional)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    // Override toString() method to provide a string representation of the Task object
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
