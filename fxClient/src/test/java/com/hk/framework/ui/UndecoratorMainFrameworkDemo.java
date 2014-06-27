package com.hk.framework.ui;

import com.hk.framework.ui.undecorator.Undecorator;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.net.URL;

/**
 * Created by jiangch on 2014/6/20.
 */
public class UndecoratorMainFrameworkDemo extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("交易系统");
        // The UI (Client Area) to display
        Region root = null;
        try {
            URL fxml = getClass().getResource("/com/hk/main/mainframe.fxml");
            FXMLLoader loader = new FXMLLoader(fxml);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            //fxmlLoader.setController(this);
            root = (Region) loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final Undecorator undecorator = new Undecorator(stage, root);
        Scene scene = new Scene(undecorator);
        undecorator.installAccelerators(scene);
        undecorator.setFadeInTransition();
  /*
         * Fade transition on window closing request
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide
                undecorator.setFadeOutTransition();
            }
        });
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(undecorator.getMinWidth());
        stage.setMinHeight(undecorator.getMinHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
