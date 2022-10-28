module com.lobbyservice.lobbyservice {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.lobbyservice.lobbyservice to javafx.fxml;
    exports com.lobbyservice.lobbyservice;
}