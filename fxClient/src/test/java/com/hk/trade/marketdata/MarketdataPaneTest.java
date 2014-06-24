package com.hk.trade.marketdata;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

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
		SimpleDoubleProperty value = new SimpleDoubleProperty();
		marketDataPane.getToggleGroup().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle2) {
				if(toggle2 instanceof MarketDataPane.DoubleValueToggleButton){
					value.unbind();
					value.set(((MarketDataPane.DoubleValueToggleButton) toggle2).getValue());
				}else if(toggle2 instanceof  MarketDataPane.LabelToggleButton){
					value.unbind();
					value.bind(((MarketDataPane.LabelToggleButton) toggle2).valueProperty());
				}
			}
		});
		value.addListener(new ChangeListener<Number>() {
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
