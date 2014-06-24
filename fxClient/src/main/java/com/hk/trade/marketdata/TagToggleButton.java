package com.hk.trade.marketdata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ToggleButton;

/**
* Created by jiangch on 2014/6/24.
*/
class TagToggleButton extends ToggleButton {

	private SimpleDoubleProperty price = new SimpleDoubleProperty();

	TagToggleButton(String s) {
		super(s);
		getStyleClass().add("market-data-button");
	}

	public SimpleDoubleProperty priceProperty() {
		return price;
	}
}
