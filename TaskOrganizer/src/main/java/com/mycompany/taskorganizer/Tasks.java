package com.mycompany.taskorganizer; // package declaration for organizing related classes and prevent naming conflicts

public class Tasks implements Task { //  Declares a public class named Tasks that implements Task interface for polymorphism
    private int id; // declares and initialize instance variables for Tasks 
    private String title;
    private String description;
    private boolean completed;

    public Tasks(int id, String title, String description) { // constructor for the Tasks class that takes three parameters: id, title, and description
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    // Getters and setters
    public int getId() { // getter method that returns id
        return id;
    }

    public String getTitle() { // getter method that returns title
        return title;
    }

    public void setTitle(String title) { // setter method that sets the title
        this.title = title;
    }

    public String getDescription() { // getter method that returns description
        return description; 
    }

    public void setDescription(String description) { // setter method that sets description
        this.description = description;
    }

    public boolean isCompleted() { // boolean flag for task complete status
        return completed;
    }

    public void setCompleted(boolean completed) { // boolean flag for task incomplete status
        this.completed = completed;
    }

    public String toString() { // returns a string representation of the Tasks object
        return "Tasks{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", completed=" + completed + '}'; // concatenates the values of id, title, description, and completed into a formatted string
    }
}