package com.hk.marketdata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;

/**
* Created by jiangch on 2014/6/24.
*/
class PriceToggleButton extends ToggleButton {

	private SimpleDoubleProperty closePrice = new SimpleDoubleProperty();
	private SimpleDoubleProperty price = new SimpleDoubleProperty();

	public static String formatText(double value){
		if (Math.abs(value) < 0.00001) {
			return "--";
		}
		return String.format("%.3f", value);
	}

	PriceToggleButton() {
		super();
		setText("--");
		getStyleClass().add("market-data-button");
		price.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				setText(formatText(price.get()));
				changePriceLabelStyle();
			}
		});
		closePrice.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				changePriceLabelStyle();
			}
		});
	}

	private void changePriceLabelStyle() {
		int result = Double.compare(getClosePrice(), getPrice());
		switch (result) {
			case 1:
				setStyle("-fx-text-fill: GREEN");
				break;
			case -1:
				setStyle("-fx-text-fill: RED");
				break;
			case 0:
				setStyle("");
				break;
		}
	}

	public double getClosePrice() {
		return closePrice.get();
	}

	public SimpleDoubleProperty closePriceProperty() {
		return closePrice;
	}

	public double getPrice() {
		return price.get();
	}

	public SimpleDoubleProperty priceProperty() {
		return price;
	}

}
