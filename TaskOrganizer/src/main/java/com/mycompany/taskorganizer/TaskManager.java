package com.mycompany.taskorganizer; // package declaration for organizing related classes and prevent naming conflicts

import java.util.ArrayList; // imports Arraylists and lists
import java.util.List;


public class TaskManager { // Declares the TaskManager class.

    private List<Tasks> tasks; // declares a private instance variable tasks that holds a list of 'Tasks' objects

    public TaskManager() { // TaskManager class constructor
        tasks = new ArrayList<>(); // initializes and empty ArrayList
    }

    public void addTask(Tasks task) { // adds a task to the list
        tasks.add(task);
    }

    public void deleteTask(Tasks task) { // deletes a task from the list
        tasks.remove(task);
    }

    public void completeTask(Tasks task) { // marks a task as completed
        task.setCompleted(true);
    }

    public void incompleteTask(Tasks task) { // marks a task as incomplete
        task.setCompleted(false);
    }

    public List<Tasks> getTasks() { // getter method that returns tasks
        return tasks;
    }

}
