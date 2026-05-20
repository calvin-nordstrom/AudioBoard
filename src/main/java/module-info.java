module com.calvinnordstrom.audioboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens com.calvinnordstrom.audioboard to javafx.fxml;
    exports com.calvinnordstrom.audioboard;
}