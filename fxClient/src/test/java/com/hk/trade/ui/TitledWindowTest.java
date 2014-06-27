package com.hk.trade.ui;

import com.hk.framework.ui.titledwindow.TitledWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by jiangch on 2014/6/25.
 */
public class TitledWindowTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		VBox vBox = new VBox();
		TitledWindow titledWindow = new TitledWindow(vBox);
		vBox.getChildren().add(titledWindow);
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		titledWindow.setTitleName("test");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
