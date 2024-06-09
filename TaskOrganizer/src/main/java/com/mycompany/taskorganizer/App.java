package com.mycompany.taskorganizer;

// Importing JavaFX classes and packages
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;

// The main application class, extending Application class from JavaFX
public class App extends Application {
    // Private field to manage tasks
    private final TaskManager taskManager = new TaskManager();
    private VBox tasksPane; // Pane to hold task cards
    
    // Override start method to initialize and configure the primary stage
    @Override
    public void start(Stage primaryStage) {
        // Set the title of the primary stage
        primaryStage.setTitle("Task Organizer");
        
        // Initialize tasks pane (VBox) with spacing and padding
        tasksPane = new VBox();
        tasksPane.setSpacing(10);
        tasksPane.setPadding(new Insets(10));
        
        // Create "Add Task" button with custom styling
        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5; -fx-border-color: #dcdcdc; " +
                "-fx-border-radius: 5; -fx-border-width: 1; -fx-padding: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);");
        addButton.setPrefSize(100, 35);
        // Set action to show add task dialog when clicked
        addButton.setOnAction(event -> showAddTaskDialog());
        
        // Create a horizontal box (HBox) to hold the "Add Task" button
        HBox topBox = new HBox(addButton);
        topBox.setAlignment(Pos.TOP_LEFT);
        topBox.setPadding(new Insets(0, 0, 0, 10));
        
        // Create a border pane to organize layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBox);
        borderPane.setCenter(tasksPane);
        
        // Create a scene with specified width and height, and set its background color
        Scene scene = new Scene(borderPane, 800, 600);
        scene.setFill(Color.WHITE);
        scene.getRoot().setStyle("-fx-background-color: #f0f0f0;");
        primaryStage.setScene(scene); // Set the scene to the primary stage
        primaryStage.show(); // Display the primary stage
    }

    // Method to add a task card to the tasks pane
    private void addTaskCard(Tasks task) {
        // Create a task card (Pane) and add it to the tasks pane
        Pane taskCard = createTaskCard(task);
        tasksPane.getChildren().add(0, taskCard); // Add new tasks to the top
    }

    // Method to create a task card (Pane) for a given task
    private Pane createTaskCard(Tasks task) {
        // Create a border pane to hold task details
        BorderPane cardPane = new BorderPane();
        cardPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5; -fx-border-color: #dcdcdc; " +
                "-fx-border-radius: 5; -fx-border-width: 1; -fx-padding: 5;");
        
        // Create labels for task title and description
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.getStyleClass().add("task-title");
        
        Label descriptionLabel = new Label(task.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(Double.MAX_VALUE);
        descriptionLabel.setMaxHeight(Double.MAX_VALUE);
        descriptionLabel.getStyleClass().add("task-description");
        
        // Create a vertical box (VBox) to hold task content
        VBox content = new VBox(titleLabel, descriptionLabel);
        content.setPadding(new Insets(5));
        
        // Bind the minimum height of the card pane to the height of the content
        cardPane.minHeightProperty().bind(content.heightProperty());
        cardPane.setCenter(content); // Set content in the center
        
        // Create circles as dots to show context menu options
        Circle dot1 = createDot();
        Circle dot2 = createDot();
        Circle dot3 = createDot();
        HBox dots = new HBox(5, dot1, dot2, dot3);
        dots.setStyle("-fx-padding: 10;");
        // Show context menu when dots are clicked
        dots.setOnMouseClicked(event -> showContextMenu(event.getScreenX(), event.getScreenY(), task, cardPane));
        cardPane.setRight(dots); // Set dots to the right
        
        // Store title and description labels as properties of the card pane
        cardPane.getProperties().put("titleLabel", titleLabel);
        cardPane.getProperties().put("descriptionLabel", descriptionLabel);
        
        return cardPane; // Return the created task card
    }

    // Method to create a dot (Circle)
    private Circle createDot() {
        Circle dot = new Circle(1.5);
        dot.setStyle("-fx-fill: black;");
        return dot;
    }

    // Method to show context menu for a task
    private void showContextMenu(double screenX, double screenY, Tasks task, Pane cardPane) {
        ContextMenu contextMenu = new ContextMenu(); // Create a context menu
        
        // Check if task is completed
        if (task.isCompleted()) {
            // If completed, show option to mark as incomplete and delete task
            MenuItem deleteItem = new MenuItem("Delete Task");
            MenuItem incompleteItem = new MenuItem("Mark as Incomplete");
            contextMenu.getItems().addAll(incompleteItem, new SeparatorMenuItem(), deleteItem);
            
            // Set action to delete task when clicked
            deleteItem.setOnAction(event -> {
                Pane parentPane = (Pane) cardPane.getParent();
                parentPane.getChildren().remove(cardPane);
                taskManager.deleteTask(task);
            });
            
            // Set action to mark task as incomplete when clicked
            incompleteItem.setOnAction(event -> incompleteTask(cardPane, task));
        } else {
            // If not completed, show options to edit, delete, and mark as complete
            MenuItem editItem = new MenuItem("Edit Task");
            MenuItem deleteItem = new MenuItem("Delete Task");
            MenuItem completeItem = new MenuItem("Mark as Complete");
            contextMenu.getItems().addAll(editItem, new SeparatorMenuItem(), completeItem, new SeparatorMenuItem(), deleteItem);
            
            // Set action to show edit/delete task window when edit is clicked
            editItem.setOnAction(event -> showEditDeleteTaskWindow(task, cardPane));
            
            // Set action to delete task when delete is clicked
            deleteItem.setOnAction(event -> {
                Pane parentPane = (Pane) cardPane.getParent();
                parentPane.getChildren().remove(cardPane);
                taskManager.deleteTask(task);
            });
            
            // Set action to mark task as complete when complete is clicked
            completeItem.setOnAction(event -> completeTask(cardPane, task));
        }
        
        // Show the context menu at the specified screen coordinates
        contextMenu.show(tasksPane.getScene().getWindow(), screenX, screenY);
    }

    // Method to show edit/delete task window
    private void showEditDeleteTaskWindow(Tasks task, Pane cardPane) {
        String originalTitle = task.getTitle();
        String originalDescription = task.getDescription();
        
        // Create a dialog to edit/delete task
        Dialog<Tasks> dialog = new Dialog<>();
        dialog.setTitle("Edit/Delete Task");
        dialog.setHeaderText("Edit or Delete Task");
        
        // Create input fields for title and description
        TextField titleField = new TextField(task.getTitle());
        TextArea descriptionArea = new TextArea(task.getDescription());
        
        // Create content pane with input fields
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Title:"), titleField, new Label("Description:"), descriptionArea);
        content.setPadding(new Insets(10));
        
        // Set content pane to the dialog
        dialog.getDialogPane().setContent(content);
        
        // Add buttons for edit, delete, and cancel
        ButtonType editButton = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(editButton, deleteButton, ButtonType.CANCEL);
        
        // Define actions for each button
        dialog.setResultConverter(buttonType -> {
            if (buttonType == editButton) {
                // If edit is clicked, update task details
                String newTitle = titleField.getText();
                String newDescription = descriptionArea.getText();
                task.setTitle(newTitle);
                task.setDescription(newDescription);
                
                // Update task card with new details
                updateTaskCard(cardPane, task, newTitle, newDescription);
                
                // Print changes to console
                System.out.println("Title changed from: " + originalTitle + " to: " + newTitle);
                System.out.println("Description changed from: " + originalDescription + " to: " + newDescription);
                
                return task;
            } else if (buttonType == deleteButton) {
                // If delete is clicked, remove task from UI and task manager
                Pane parentPane = (Pane) cardPane.getParent();
                parentPane.getChildren().remove(cardPane);
                taskManager.deleteTask(task);
            }
            
            return null;
        });
        
        // Show the dialog and wait for user response
        dialog.showAndWait();
    }

    // Method to show add task dialog
    private void showAddTaskDialog() {
        try {
            // Create a dialog to add a new task
            Dialog<Tasks> dialog = new Dialog<>();
            dialog.setTitle("Add Task");
            dialog.setHeaderText("Enter Task Details");
            
            // Create input fields for title and description
            TextField titleField = new TextField();
            titleField.setPromptText("Enter task title");
            
            TextArea descriptionArea = new TextArea();
            descriptionArea.setPromptText("Enter task description");
            
            // Create content pane with input fields
            VBox content = new VBox(10);
            content.getChildren().addAll(new Label("Title:"), titleField, new Label("Description:"), descriptionArea);
            content.setPadding(new Insets(10));
            
            // Set content pane to the dialog
            dialog.getDialogPane().setContent(content);
            
            // Add buttons for add and cancel
            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);
            
            // Define action for add button
            dialog.setResultConverter(buttonType -> {
                if (buttonType == addButton) {
                    // If add is clicked, create a new task and add it to UI and task manager
                    String title = titleField.getText();
                    String description = descriptionArea.getText();
                    Tasks task = new Tasks(taskManager.getTasks().size() + 1, title, description);
                    taskManager.addTask(task);
                    return task;
                }
                return null;
            });
            
            // Show the dialog and wait for user response
            dialog.showAndWait().ifPresent(this::addTaskCard);
        } catch (Exception e) {
            // Handle any exceptions that occur during dialog creation
            e.printStackTrace();
            // Show an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }

    // Method to update task card with new title and description
    private void updateTaskCard(Pane cardPane, Tasks task, String newTitle, String newDescription) {
        // Retrieve title and description labels from card pane properties
        Label titleLabel = (Label) cardPane.getProperties().get("titleLabel");
        Label descriptionLabel = (Label) cardPane.getProperties().get("descriptionLabel");
        
        // Update title label if not null
        if (titleLabel != null) {
            titleLabel.setText(newTitle);
        }
        
        // Update description label if not null
        if (descriptionLabel != null) {
            descriptionLabel.setText(newDescription);
        }
    }

    // Method to mark task as complete
    private void completeTask(Pane cardPane, Tasks task) {
        task.setCompleted(true); // Set task as completed
        updateTaskCardStyle(cardPane, true); // Update card style
        tasksPane.getChildren().remove(cardPane); // Remove card from UI
        tasksPane.getChildren().add(cardPane); // Add card to UI (at bottom)
    }

    // Method to mark task as incomplete
    private void incompleteTask(Pane cardPane, Tasks task) {
        task.setCompleted(false); // Set task as incomplete
        updateTaskCardStyle(cardPane, false); // Update card style
        tasksPane.getChildren().remove(cardPane); // Remove card from UI
        tasksPane.getChildren().add(0, cardPane); // Add card to UI (at top)
    }

    // Method to update task card style based on completion status
    private void updateTaskCardStyle(Pane cardPane, boolean completed) {
        if (completed) {
            // If completed, set card style to gray background
            cardPane.setStyle("-fx-background-color: #dcdcdc; -fx-background-radius: 5;" +
                    "-fx-border-radius: 5; -fx-border-width: 1; -fx-padding: 5;");
            
            // Retrieve title and description labels from card pane properties
            Label titleLabel = (Label) cardPane.getProperties().get("titleLabel");
            Label descriptionLabel = (Label) cardPane.getProperties().get("descriptionLabel");
            
            // Update title label style if not null
            if (titleLabel != null) {
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: darkgrey;");
            }
            
            // Update description label style if not null
            if (descriptionLabel != null) {
                descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: darkgrey;");
            }
        } else {
            // If incomplete, set card style to white background
            cardPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5; -fx-border-color: #dcdcdc; " +
                    "-fx-border-radius: 5; -fx-border-width: 1; -fx-padding: 5;");
            
            // Retrieve title and description labels from card pane properties
            Label titleLabel = (Label) cardPane.getProperties().get("titleLabel");
            Label descriptionLabel = (Label) cardPane.getProperties().get("descriptionLabel");
            
            // Update title label style if not null
            if (titleLabel != null) {
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            }
            
            // Update description label style if not null
            if (descriptionLabel != null) {
                descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
            }
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
