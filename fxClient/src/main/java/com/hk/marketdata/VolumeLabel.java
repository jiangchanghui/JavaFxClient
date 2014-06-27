package com.hk.marketdata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
* Created by jiangch on 2014/6/24.
*/
class VolumeLabel extends Label {
	private SimpleIntegerProperty value = new SimpleIntegerProperty();
	private SimpleIntegerProperty max = new SimpleIntegerProperty();
	private MarketDataPane.DepthSide depthSide;

	public VolumeLabel(MarketDataPane.DepthSide depthSide) {
		super();
		setText("--");
		this.depthSide = depthSide;
		setAlignment(Pos.CENTER_RIGHT);
		value.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				updateText(number2);
				updateColorBar();
			}

		});
		max.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				updateColorBar();
			}
		});
	}

	private void updateText(Number number2) {
		if (number2.intValue() == 0) {
			setText("--");
		} else {
			setText(number2.toString());
		}
	}

	public void updateColorBar() {
		if (value.intValue() == 0) {
			setMinWidth(20);
			setStyle("");
			return;
		}
		setMinWidth(20 + 60.0 * value.intValue() / max.getValue());
		if (depthSide == MarketDataPane.DepthSide.SELL) {
			setStyle("-fx-background-color: #ffebeb");
		} else {
			setStyle("-fx-background-color: #ecfff8");
		}
	}

	public int getValue() {
		return value.get();
	}

	public SimpleIntegerProperty valueProperty() {
		return value;
	}

	public SimpleIntegerProperty maxProperty() {
		return max;
	}

}
