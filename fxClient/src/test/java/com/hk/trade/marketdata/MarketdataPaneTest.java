package com.hk.trade.marketdata;

import com.hk.marketdata.MarketDataPane;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MarketdataPaneTest extends Application {


	@Override
	public void start(Stage stage) throws Exception {
		MarketDataPane marketDataPane = new MarketDataPane();
		Scene scene = new Scene(marketDataPane);
		stage.setScene(scene);
		stage.show();

		marketDataPane.selectedValueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				System.out.println(number2.doubleValue());

			}
		});
		marketDataPane.start();

	}




	public static void main(String[] args) {
		Application.launch(args);
	}

}
