package com.hk.marketdata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MarketDepth {

	private SimpleDoubleProperty price = new SimpleDoubleProperty();
	private SimpleIntegerProperty volume = new SimpleIntegerProperty();

	public double getPrice() {
		return price.get();
	}

	public SimpleDoubleProperty priceProperty() {
		return price;
	}

	public void setPrice(double price) {
		this.price.set(price);
	}

	public int getVolume() {
		return volume.get();
	}

	public SimpleIntegerProperty volumeProperty() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume.set(volume);
	}
}
