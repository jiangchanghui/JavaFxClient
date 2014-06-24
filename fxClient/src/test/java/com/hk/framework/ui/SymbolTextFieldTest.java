package com.hk.framework.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by jiangch on 2014/6/24.
 */
public class SymbolTextFieldTest extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		VBox vBox = new VBox();
		vBox.getChildren().add(new SymbolTextField());
		VBox e = new VBox();
		e.setMinHeight(200);
		vBox.getChildren().add(e);
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
