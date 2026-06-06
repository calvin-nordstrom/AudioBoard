module com.calvinnordstrom.audioboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires com.fazecast.jSerialComm;
    requires jnativehook;
    requires java.logging;
    requires java.desktop;


    opens com.calvinnordstrom.audioboard to javafx.fxml;
    exports com.calvinnordstrom.audioboard;
}