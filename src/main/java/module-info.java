module com.example.project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;


    opens com.example.project3 to javafx.fxml;
    exports com.example.project3;
    exports com.example.project3.graph;
    opens com.example.project3.graph to javafx.fxml;
}