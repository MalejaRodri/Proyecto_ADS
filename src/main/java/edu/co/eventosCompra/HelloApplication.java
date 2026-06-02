package edu.co.eventosCompra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/edu/co/PANTALLAS/Ingreso.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 280, 400);
        stage.setTitle("Cine El MAster");
        stage.setScene(scene);
        stage.show();
    }


}
