package com.mycompany.taskorganizer; // package declaration for organizing related classes and prevent naming conflicts

public interface Task { //  declares a Tasl public interface
    int getId(); // declares getter, setter methods and boolean flags that return their corresponding types
    String getTitle();
    String getDescription();
    boolean isCompleted();
    void setCompleted(boolean completed);
}
