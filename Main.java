package rw.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final String version = "1.0";

    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     * @author M.H. 2025.04.01
     * https://csgit.ucalgary.ca/maryna.hez/cpsc233w25a3-maryna-hez.git
     */

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //Students edit here to set up the scene
        stage.setScene(scene); //The whole picture of demonstration
        stage.setTitle("Robot Wars World Editor v" + version); //Add a title of GUI
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show(); //Show made window
    }
}