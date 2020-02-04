/*
 * CS 300-A, 2017S
 */
package scaleplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This JavaFX app lets the user play scales.
 * @author Janet Davis 
 * @author Maxwell Brown
 * @author Dexter Aichele
 * @author Trung Vu
 * @since 2017-01-26
 */
public class ScalePlayer extends Application {

    // 0 2 4 5 7 9 11 12

    @Override
    public void start(Stage stage) {
        Button playScaleButton = new Button("Play scale");
        Button stopPlayingButton = new Button("Stop playing");
        playScaleButton.setOnAction(event -> {
            System.out.println("Hello World!");
        });
        BorderPane borderPane = createBorderPane(stage);
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);
        hBox.getChildren().addAll(playScaleButton, stopPlayingButton);
        Scene scene = new Scene(borderPane, 300, 250);
        stage.setTitle("Scale Player");
        stage.setScene(scene);
        stage.show();
    }

    public BorderPane createBorderPane(Stage stage) {
        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        borderPane.setTop(menuBar);
        Menu fileMenu = new Menu("File");
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.setOnAction(event -> Platform.exit());
        fileMenu.getItems().add(quitMenuItem);
        menuBar.getMenus().add(fileMenu);
        return borderPane;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
