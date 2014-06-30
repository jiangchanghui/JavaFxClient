package com.hk.trade.order;

import com.hk.framework.FxmlLoadUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by jiangch on 2014/6/26.
 */
public class StockOrderTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Pane pane = FxmlLoadUtils.loadFxml("/com/hk/trade/order/stockordermain.fxml").getPane();
		System.out.println(pane);
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
