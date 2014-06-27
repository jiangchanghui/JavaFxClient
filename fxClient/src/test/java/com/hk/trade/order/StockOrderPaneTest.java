package com.hk.trade.order;

import com.hk.framework.FxmlLoadUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by jiangch on 2014/6/27.
 */
public class StockOrderPaneTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new StockOrderPane());
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
