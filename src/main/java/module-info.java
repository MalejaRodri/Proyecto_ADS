module edu.co.eventosCompra {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens edu.co.eventosCompra to javafx.fxml;
    exports edu.co.eventosCompra;
}