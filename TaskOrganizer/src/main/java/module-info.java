module com.mycompany.taskorganizer {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.taskorganizer to javafx.fxml;
    exports com.mycompany.taskorganizer;
}
