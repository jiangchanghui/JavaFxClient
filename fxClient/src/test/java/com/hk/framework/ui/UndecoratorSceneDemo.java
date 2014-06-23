/**
 * Demo purpose In-SideFX (Un)decorator for JavaFX scene License: You can use
 * this code for any kind of purpose, commercial or not.
 */
package com.hk.framework.ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author in-sideFX
 */
public class UndecoratorSceneDemo extends Application {

    Stage primaryStage;
    @FXML
    private AreaChart areaChart;
    @FXML
    private PieChart pieChart;

    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void start(final Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Undecorator Scene Demo");

        // The UI (Client Area) to display
        Region root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientArea.fxml"));
            fxmlLoader.setController(this);
            root = (Region) fxmlLoader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // The Undecorator as a Scene
        final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, root);

        // Enable fade transition
        undecoratorScene.setFadeInTransition();

        // Optional: Enable this node to drag the primaryStage
        // By default the root argument of Undecorator is set as draggable
//        Node node = root.lookup("#draggableNode");
//        undecoratorScene.setAsStageDraggable(primaryStage, node);

        /*
         * Fade transition on window closing request
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide
                undecoratorScene.setFadeOutTransition();
            }
        });

        // Application icons
        Image image = new Image("/demoapp/in-sidefx.png");
        primaryStage.getIcons().addAll(image);

        primaryStage.setScene(undecoratorScene);
        primaryStage.sizeToScene();
        primaryStage.toFront();

        // Set minimum size based on client area's minimum sizes
        Undecorator undecorator = undecoratorScene.getUndecorator();
        primaryStage.setMinWidth(undecorator.getMinWidth());
        primaryStage.setMinHeight(undecorator.getMinHeight());

        // Feed Charts with fake data for demo
        initCharts();
        primaryStage.show();
    }

    /**
     * The button's handler in the ClientArea.fxml Manage the UTILITY mode
     * stages
     *
     * @param event
     */
    @FXML
    @SuppressWarnings("CallToThreadDumpStack")
    private void handleShowUtilityStage(ActionEvent event) {
        // Stage Utility usage
        Region root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientAreaUtility.fxml"));
            fxmlLoader.setController(this);
            root = (Region) fxmlLoader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Stage utilityStage = new Stage();
        utilityStage.setTitle("Stage Utility type demo");
        UndecoratorScene scene = new UndecoratorScene(utilityStage, StageStyle.UTILITY, root, null);
        utilityStage.setScene(scene);
        utilityStage.initModality(Modality.WINDOW_MODAL);
        utilityStage.initOwner(primaryStage);

        // Set sizes based on client area's sizes
        Undecorator undecorator = scene.getUndecorator();
        utilityStage.setMinWidth(undecorator.getMinWidth());
        utilityStage.setMinHeight(undecorator.getMinHeight());
        utilityStage.setWidth(undecorator.getPrefWidth());
        utilityStage.setHeight(undecorator.getPrefHeight());
        if (undecorator.getMaxWidth() > 0) {
            utilityStage.setMaxWidth(undecorator.getMaxWidth());
        }
        if (undecorator.getMaxHeight() > 0) {
            utilityStage.setMaxHeight(undecorator.getMaxHeight());
        }
        utilityStage.sizeToScene();
        utilityStage.show();
    }

    /**
     * Show a non resizable Stage
     *
     * @param event
     */
    @FXML
    @SuppressWarnings("CallToThreadDumpStack")
    private void handleShowNonResizableStage(ActionEvent event) {
        UndecoratorSceneDemo undecoratorSceneDemo = new UndecoratorSceneDemo();
        Stage stage = new Stage();
        stage.setTitle("Not resizable primaryStage");
        stage.setResizable(false);
        stage.setWidth(600);
        stage.setMinHeight(400);
        try {
            undecoratorSceneDemo.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles Utility primaryStage buttons
     *
     * @param event
     */
    public void handleUtilityAction(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Demo purpose only, Fill charts with data
     */
    void initCharts() {
        if (areaChart == null) {
            return;
        }
        final NumberAxis xAxis = new NumberAxis(1, 30, 1);
        final NumberAxis yAxis = new NumberAxis(-5, 27, 5);
        xAxis.setForceZeroInRange(true);

        areaChart.setTitle("Temperature Monitoring (in Degrees C)");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("March");
        series1.getData().add(new XYChart.Data(1, -2));
        series1.getData().add(new XYChart.Data(3, -4));
        series1.getData().add(new XYChart.Data(6, 0));
        series1.getData().add(new XYChart.Data(9, 5));
        series1.getData().add(new XYChart.Data(12, -4));
        series1.getData().add(new XYChart.Data(15, 6));
        series1.getData().add(new XYChart.Data(18, 8));
        series1.getData().add(new XYChart.Data(21, 14));
        series1.getData().add(new XYChart.Data(24, 4));
        series1.getData().add(new XYChart.Data(27, 6));
        series1.getData().add(new XYChart.Data(30, 6));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("April");
        series2.getData().add(new XYChart.Data(1, 4));
        series2.getData().add(new XYChart.Data(3, 10));
        series2.getData().add(new XYChart.Data(6, 15));
        series2.getData().add(new XYChart.Data(9, 8));
        series2.getData().add(new XYChart.Data(12, 5));
        series2.getData().add(new XYChart.Data(15, 18));
        series2.getData().add(new XYChart.Data(18, 15));
        series2.getData().add(new XYChart.Data(21, 13));
        series2.getData().add(new XYChart.Data(24, 19));
        series2.getData().add(new XYChart.Data(27, 21));
        series2.getData().add(new XYChart.Data(30, 21));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("May");
        series3.getData().add(new XYChart.Data(1, 20));
        series3.getData().add(new XYChart.Data(3, 15));
        series3.getData().add(new XYChart.Data(6, 13));
        series3.getData().add(new XYChart.Data(9, 12));
        series3.getData().add(new XYChart.Data(12, 14));
        series3.getData().add(new XYChart.Data(15, 18));
        series3.getData().add(new XYChart.Data(18, 25));
        series3.getData().add(new XYChart.Data(21, 25));
        series3.getData().add(new XYChart.Data(24, 23));
        series3.getData().add(new XYChart.Data(27, 26));
        series3.getData().add(new XYChart.Data(30, 26));

        areaChart.setHorizontalZeroLineVisible(true);
        areaChart.getData().addAll(series1, series2, series3);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
        pieChart.setData(pieChartData);
        pieChart.setTitle("Imported Fruits");

    }

    public static void main(String[] args) {
        launch(args);
    }
}
