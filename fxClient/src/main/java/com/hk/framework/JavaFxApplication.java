package com.hk.framework;

import bibliothek.gui.DockController;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.station.split.SplitDockProperty;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public abstract class JavaFxApplication extends Application {

    abstract protected Configuration buildGlobalConfiguration();

    protected Stage stage;

    protected Configuration globalConfiguration;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        this.globalConfiguration = buildGlobalConfiguration();
        setUserAgentStylesheet(STYLESHEET_MODENA);
        switchScene(globalConfiguration.getRootSceneName());
        stage.show();
    }

    public JavaFxController switchScene(String sceneName) {
        return switchScene(sceneName, globalConfiguration);
    }


    public JavaFxController switchScene(String sceneName, Configuration sceneConf) {
        URL fxml = getClass().getResource(sceneName + sceneConf.getFxmlExtension());
        FXMLLoader loader = new FXMLLoader(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            Pane pane = (Pane) loader.load();
            Scene scene = new Scene(pane);
            if (sceneConf.getStylesheetNames() != null) {
                for (String stylesheetName : sceneConf.getStylesheetNames()) {
                    scene.getStylesheets().add(
                            getClass().getResource(stylesheetName)
                                    .toExternalForm());
                }
            }
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JavaFxController.class.cast(loader.getController()).setUp(this);
    }


    public static class Configuration extends BaseConfiguration {
        private double sceneWidth;
        private double sceneHeight;
        private String[] stylesheetNames;

        public double getSceneWidth() {
            return sceneWidth;
        }

        public double getSceneHeight() {
            return sceneHeight;
        }

        public String[] getStylesheetNames() {
            return stylesheetNames;
        }

        public Configuration(double sceneWidth, double sceneHeight) {
            this(sceneWidth, sceneHeight, new String[] {});
        }

        public Configuration(double sceneWidth, double sceneHeight,
                             String... stylesheetNames) {
            this("", "", sceneWidth, sceneHeight, stylesheetNames);
        }

        public Configuration(String applicationTitle, String rootSceneName,
                             double sceneWidth, double sceneHeight) {
            this(applicationTitle, rootSceneName, sceneWidth, sceneHeight,
                    new String[] {});
        }

        public Configuration(String applicationTitle, String rootSceneName,
                             double sceneWidth, double sceneHeight,
                             String... stylesheetNames) {
            super(applicationTitle, rootSceneName);
            this.sceneWidth = sceneWidth;
            this.sceneHeight = sceneHeight;
            this.stylesheetNames = stylesheetNames;
        }
    }


    public static abstract class BaseConfiguration {
        private String applicationTitle;
        private String rootSceneName;
        private String fxmlExtension;

        public BaseConfiguration(String applicationTitle, String rootSceneName) {
            this.applicationTitle = applicationTitle;
            this.rootSceneName = rootSceneName;
            this.fxmlExtension = ".fxml";
        }

        public String getApplicationTitle() {
            return applicationTitle;
        }

        public String getRootSceneName() {
            return rootSceneName;
        }

        public String getFxmlExtension() {
            return fxmlExtension;
        }
    }

}
