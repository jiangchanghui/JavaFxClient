package com.hk.trade.marketdata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.tools.Borders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MarketDataPane extends VBox {
	private ToggleGroup toggleGroup = new ToggleGroup();
	private TextField symbolTextField = new TextField();
	private List<ToggleButton> priceButtons = new ArrayList<ToggleButton>();
	private MarketData marketData = new MarketData();
	private MarketDataUpdateService marketDataUpdateService = new MarketDataUpdateService();

	public MarketDataPane() {
		super();
		getStylesheets().add("com/hk/trade/marketdata/marketdata.css");
		TextFields.bindAutoCompletion(
				symbolTextField,
				"Hey", "Hello", "Hello World", "Apple", "Cool", "Costa", "Cola", "Coca Cola");
		getChildren().add(symbolTextField);
		ColumnConstraints firstColumn = getColumnConstraints(40);
		firstColumn.setHalignment(HPos.LEFT);
		ColumnConstraints secondColumn = getColumnConstraints(80);
		secondColumn.setHalignment(HPos.RIGHT);
		ColumnConstraints lastColumn = getColumnConstraints(100);
		lastColumn.setHalignment(HPos.RIGHT);

		GridPane sellDepthGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addMarketDepth(sellDepthGridPane, "Âô¢Ý", 0, DepthSide.SELL, 4);
		addMarketDepth(sellDepthGridPane, "Âô¢Ü", 1, DepthSide.SELL, 3);
		addMarketDepth(sellDepthGridPane, "Âô¢Û", 2, DepthSide.SELL, 2);
		addMarketDepth(sellDepthGridPane, "Âô¢Ú", 3, DepthSide.SELL, 1);
		addMarketDepth(sellDepthGridPane, "Âô¢Ù", 4, DepthSide.SELL, 0);
		getChildren().add(Borders.wrap(sellDepthGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).buildAll());

		GridPane latestPriceGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addLatestPrice(latestPriceGridPane, "×îÐÂ");
		getChildren().add(Borders.wrap(latestPriceGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());

		GridPane buyDepthGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addMarketDepth(buyDepthGridPane, "Âò¢Ù", 0, DepthSide.BUY, 0);
		addMarketDepth(buyDepthGridPane, "Âò¢Ú", 1, DepthSide.BUY, 1);
		addMarketDepth(buyDepthGridPane, "Âò¢Û", 2, DepthSide.BUY, 2);
		addMarketDepth(buyDepthGridPane, "Âò¢Ü", 3, DepthSide.BUY, 3);
		addMarketDepth(buyDepthGridPane, "Âò¢Ý", 4, DepthSide.BUY, 4);
		getChildren().add(Borders.wrap(buyDepthGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).buildAll());

		ColumnConstraints titleColumn = getColumnConstraints(40);
		titleColumn.setHalignment(HPos.LEFT);
		ColumnConstraints valueColumn = getColumnConstraints(80);
		valueColumn.setHalignment(HPos.RIGHT);
		ColumnConstraints titleColumn2 = getColumnConstraints(40);
		titleColumn.setHalignment(HPos.LEFT);
		ColumnConstraints valueColumn2 = getColumnConstraints(60);
		valueColumn.setHalignment(HPos.RIGHT);
		GridPane dailyLimitGridPane = new GridPane();
		dailyLimitGridPane.setHgap(2);
		dailyLimitGridPane.getColumnConstraints().addAll(titleColumn, valueColumn, titleColumn2, valueColumn2);
		addDailyLimit(dailyLimitGridPane, "ÕÇÍ£", "µøÍ£");
		getChildren().add(Borders.wrap(dailyLimitGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());
		GridPane openClosePriceGridPane = new GridPane();
		openClosePriceGridPane.setHgap(2);
		openClosePriceGridPane.getColumnConstraints().addAll(titleColumn, valueColumn, titleColumn2, valueColumn2);
		addOpenClosePrice(openClosePriceGridPane, "×òÊÕ", "½ñ¿ª");
		getChildren().add(Borders.wrap(openClosePriceGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());
		marketData.closePriceProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				for(ToggleButton button : priceButtons){
					addPriceStyle(number2,button);
				}
			}
		});
		marketDataUpdateService.valueProperty().addListener(new ChangeListener<ObservableList<MarketData>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<MarketData>> observableValue, ObservableList<MarketData> marketDatas, ObservableList<MarketData> marketDatas2) {
				if (marketDatas2 != null) {
					updateMarketData(marketDatas2.get(0));
				}
			}
		});
		marketDataUpdateService.selectedSymbolProperty().bind(symbolTextField.textProperty());
		marketDataUpdateService.setPeriod(Duration.seconds(30));
	}

	private void addOpenClosePrice(GridPane gridPane, String closePriceName, String openPriceName) {
		ToggleButton closePriceLabel = createLabelToggleButton(closePriceName);
		gridPane.add(closePriceLabel, 0, 0);
		ToggleButton closePriceValue = createValueToggleButton();
		gridPane.add(closePriceValue, 1, 0);
		addPriceListener(marketData.closePriceProperty(), closePriceValue);
		ToggleButton openPriceLabel = createLabelToggleButton(openPriceName);
		gridPane.add(openPriceLabel, 2, 0);
		ToggleButton openPriceValue = createValueToggleButton();
		addPriceListener(marketData.openPriceProperty(), openPriceValue);
		gridPane.add(openPriceValue, 3, 0);
		toggleGroup.getToggles().addAll(closePriceLabel, closePriceValue, openPriceLabel, openPriceValue);
	}

	public void start() {
		marketDataUpdateService.start();
	}

	private GridPane getGridPane(ColumnConstraints firstColumn, ColumnConstraints secondColumn, ColumnConstraints lastColumn) {
		GridPane buyDepthGridPane = new GridPane();
		buyDepthGridPane.getColumnConstraints().addAll(firstColumn, secondColumn, lastColumn);
		buyDepthGridPane.setVgap(1);
		buyDepthGridPane.setHgap(1);
		return buyDepthGridPane;
	}

	private ColumnConstraints getColumnConstraints(int minWidth) {
		ColumnConstraints lastColumn = new ColumnConstraints();
		lastColumn.setMinWidth(minWidth);
		lastColumn.setHgrow(Priority.SOMETIMES);
		return lastColumn;
	}

	private void addMarketDepth(GridPane gridPane, String name, int rowNum, DepthSide depthSide, int depth) {
		ToggleButton nameLabel = createLabelToggleButton(name);
		gridPane.add(nameLabel, 0, rowNum);
		ToggleButton priceLabel = createValueToggleButton();
		gridPane.add(priceLabel, 1, rowNum);
		addListener(priceLabel, depthSide, depth, DepthType.PRICE);
		Label label = new Label("--");
		addListener(label, depthSide, depth, DepthType.VOLUME);
		gridPane.add(label, 2, rowNum);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
	}

	private void addLatestPrice(GridPane gridPane, String name) {
		ToggleButton nameLabel = createLabelToggleButton(name);
		gridPane.add(nameLabel, 0, 0);
		ToggleButton priceLabel = createValueToggleButton();
		gridPane.add(priceLabel, 1, 0);
		addPriceListener(marketData.latestPriceProperty(), priceLabel);
		Label label = new Label("--");
		marketData.priceDeltaProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				label.setText(number2.toString());
			}
		});
		gridPane.add(label, 2, 0);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
		nameLabel.styleProperty().bind(priceLabel.styleProperty());
		label.styleProperty().bind(priceLabel.styleProperty());
	}

	private void addListener(final Labeled priceLabel, DepthSide depthSide, int depth, DepthType depthType) {
		if (depthType == DepthType.PRICE) {
			if (depthSide == DepthSide.SELL) {
				addPriceListener(marketData.askPrice(depth), priceLabel);
			} else {
				addPriceListener(marketData.bidPrice(depth), priceLabel);
			}
		} else {
			if (depthSide == DepthSide.SELL) {
				addVolumeListener(priceLabel, depthType, marketData.askVolume(depth));
			} else {
				addVolumeListener(priceLabel, depthType, marketData.bidVolume(depth));
			}
		}
	}

	private void addVolumeListener(final Labeled priceLabel, final DepthType depthType, SimpleIntegerProperty simpleIntegerProperty) {
		simpleIntegerProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				priceLabel.setText(number2.toString());

			}
		});
	}

	private void addPriceListener(SimpleDoubleProperty simpleDoubleProperty, final Labeled priceLabel) {
		simpleDoubleProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				priceLabel.setText(number2.toString());
				addPriceStyle(number2, priceLabel);
			}
		});
	}

	private ToggleButton createLabelToggleButton(String name) {
		ToggleButton toggleButton = new ToggleButton(name);
		toggleButton.getStyleClass().add("market-data-button");
		return toggleButton;
	}

	private ToggleButton createValueToggleButton() {
		ToggleButton toggleButton = new ToggleButton("--");
		toggleButton.getStyleClass().add("market-data-button");
		priceButtons.add(toggleButton);
		return toggleButton;
	}

	private void addDailyLimit(GridPane gridPane, String title1, String title2) {
		ToggleButton nameLabel = createLabelToggleButton(title1);
		gridPane.add(nameLabel, 0, 0);
		ToggleButton priceLabel = createValueToggleButton();
		addPriceListener(marketData.dailyUpLimitPriceProperty(), priceLabel);
		gridPane.add(priceLabel, 1, 0);

		ToggleButton nameLabel2 = createLabelToggleButton(title2);
		gridPane.add(nameLabel2, 2, 0);
		ToggleButton priceLabel2 = createValueToggleButton();
		addPriceListener(marketData.dailyDownLimitPriceProperty(), priceLabel2);

		gridPane.add(priceLabel2, 3, 0);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
		toggleGroup.getToggles().addAll(nameLabel2, priceLabel2);
	}

	private void addPriceStyle(Number number2, Labeled priceLabel) {
		if (marketData.getClosePrice() > number2.doubleValue()) {
			priceLabel.setStyle("-fx-text-fill: GREEN");
		} else if (marketData.getClosePrice() < number2.doubleValue()) {
			priceLabel.setStyle("-fx-text-fill: RED");
		} else {
			priceLabel.setStyle("");
		}
	}


	private void addPriceStyle(Number number2, ToggleButton button) {
		try {
			Double currentValue = Double.valueOf(button.getText().toString());
			if(currentValue==null){
				return;
			}
			if (number2.doubleValue() > currentValue) {
				button.setStyle("-fx-text-fill: GREEN");
			} else if (number2.doubleValue() < currentValue) {
				button.setStyle("-fx-text-fill: RED");
			} else {
				button.setStyle("");
			}
		}catch (Exception e){
			return;
		}

	}

	public void updateMarketData(MarketData marketData) {
		this.marketData.update(marketData);
	}

	enum DepthSide {
		SELL, BUY
	}

	enum DepthType {
		PRICE, VOLUME
	}
}
