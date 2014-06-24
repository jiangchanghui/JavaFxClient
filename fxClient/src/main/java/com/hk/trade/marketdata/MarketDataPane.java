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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MarketDataPane extends VBox {
	private ToggleGroup toggleGroup = new ToggleGroup();
	private TextField symbolTextField = new TextField();
	private List<DoubleValueToggleButton> priceButtons = new ArrayList<DoubleValueToggleButton>();
	private MarketData marketData = new MarketData();
	private MarketDataUpdateService marketDataUpdateService = new MarketDataUpdateService();

	public MarketDataPane() {
		super();
		addCss();
		initSymbolInputPart();
		initMarketDepthPart();
		initStaticMarketDataPart();
		initClosePriceListener();
		initUpdateService();
	}

	public ToggleGroup getToggleGroup() {
		return toggleGroup;
	}

	private void addCss() {
		getStylesheets().add("com/hk/trade/marketdata/marketdata.css");
	}

	private void initUpdateService() {
		marketDataUpdateService.valueProperty().addListener(new ChangeListener<ObservableList<MarketData>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<MarketData>> observableValue, ObservableList<MarketData> marketDatas, ObservableList<MarketData> marketDatas2) {
				if (marketDatas2 != null) {
					updateMarketData(marketDatas2.get(0));
				}
			}
		});
		marketDataUpdateService.selectedSymbolProperty().bind(symbolTextField.textProperty());
		marketDataUpdateService.setPeriod(Duration.seconds(5));
	}

	private void initClosePriceListener() {
		marketData.closePriceProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				for (DoubleValueToggleButton button : priceButtons) {
					double currentValue = button.getValue();
					changePriceLabelStyle(button, number2.doubleValue(), currentValue);
				}
			}
		});
	}

	private void initSymbolInputPart() {
		TextFields.bindAutoCompletion(
				symbolTextField,
				"Hey", "Hello", "Hello World", "Apple", "Cool", "Costa", "Cola", "Coca Cola");
		getChildren().add(symbolTextField);
	}

	private void initStaticMarketDataPart() {
		ColumnConstraints titleColumn = getColumnConstraints(40);
		titleColumn.setHalignment(HPos.LEFT);
		ColumnConstraints valueColumn = getColumnConstraints(80);
		valueColumn.setHalignment(HPos.RIGHT);
		ColumnConstraints titleColumn2 = getColumnConstraints(40);
		titleColumn.setHalignment(HPos.LEFT);
		ColumnConstraints valueColumn2 = getColumnConstraints(60);
		valueColumn.setHalignment(HPos.RIGHT);
		addDailyLimitPart(titleColumn, valueColumn, titleColumn2, valueColumn2);
		addOpenClosePricePart(titleColumn, valueColumn, titleColumn2, valueColumn2);
	}

	private void addOpenClosePricePart(ColumnConstraints titleColumn, ColumnConstraints valueColumn, ColumnConstraints titleColumn2, ColumnConstraints valueColumn2) {
		GridPane openClosePriceGridPane = new GridPane();
		openClosePriceGridPane.setHgap(2);
		openClosePriceGridPane.getColumnConstraints().addAll(titleColumn, valueColumn, titleColumn2, valueColumn2);
		LabelToggleButton closePriceLabel = createLabelToggleButton("×òÊÕ");
		closePriceLabel.bind(marketData.closePriceProperty());
		openClosePriceGridPane.add(closePriceLabel, 0, 0);
		DoubleValueToggleButton closePriceValue = createValueToggleButton();
		openClosePriceGridPane.add(closePriceValue, 1, 0);
		addMarketPriceListener(marketData.closePriceProperty(), closePriceValue);
		LabelToggleButton openPriceLabel = createLabelToggleButton("½ñ¿ª");
		openPriceLabel.bind(marketData.openPriceProperty());
		openClosePriceGridPane.add(openPriceLabel, 2, 0);
		DoubleValueToggleButton openPriceValue = createValueToggleButton();
		addMarketPriceListener(marketData.openPriceProperty(), openPriceValue);
		openClosePriceGridPane.add(openPriceValue, 3, 0);
		toggleGroup.getToggles().addAll(closePriceLabel, closePriceValue, openPriceLabel, openPriceValue);
		getChildren().add(Borders.wrap(openClosePriceGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());
	}

	private void addDailyLimitPart(ColumnConstraints titleColumn, ColumnConstraints valueColumn, ColumnConstraints titleColumn2, ColumnConstraints valueColumn2) {
		GridPane dailyLimitGridPane = new GridPane();
		dailyLimitGridPane.setHgap(2);
		dailyLimitGridPane.getColumnConstraints().addAll(titleColumn, valueColumn, titleColumn2, valueColumn2);
		LabelToggleButton nameLabel = createLabelToggleButton("ÕÇÍ£");
		nameLabel.bind(marketData.dailyUpLimitPriceProperty());
		dailyLimitGridPane.add(nameLabel, 0, 0);
		DoubleValueToggleButton priceLabel = createValueToggleButton();
		addMarketPriceListener(marketData.dailyUpLimitPriceProperty(), priceLabel);
		dailyLimitGridPane.add(priceLabel, 1, 0);

		LabelToggleButton nameLabel2 = createLabelToggleButton("µøÍ£");
		nameLabel2.bind(marketData.dailyDownLimitPriceProperty());
		dailyLimitGridPane.add(nameLabel2, 2, 0);
		DoubleValueToggleButton priceLabel2 = createValueToggleButton();
		addMarketPriceListener(marketData.dailyDownLimitPriceProperty(), priceLabel2);

		dailyLimitGridPane.add(priceLabel2, 3, 0);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
		toggleGroup.getToggles().addAll(nameLabel2, priceLabel2);
		getChildren().add(Borders.wrap(dailyLimitGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());
	}

	private void initMarketDepthPart() {
		ColumnConstraints firstColumn = getColumnConstraints(40);
		firstColumn.setHalignment(HPos.LEFT);
		ColumnConstraints secondColumn = getColumnConstraints(80);
		secondColumn.setHalignment(HPos.RIGHT);
		ColumnConstraints lastColumn = getColumnConstraints(100);
		lastColumn.setHalignment(HPos.RIGHT);

		GridPane sellDepthGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addMarketDepthPart(sellDepthGridPane, "Âô¢Ý", 0, DepthSide.SELL, 4);
		addMarketDepthPart(sellDepthGridPane, "Âô¢Ü", 1, DepthSide.SELL, 3);
		addMarketDepthPart(sellDepthGridPane, "Âô¢Û", 2, DepthSide.SELL, 2);
		addMarketDepthPart(sellDepthGridPane, "Âô¢Ú", 3, DepthSide.SELL, 1);
		addMarketDepthPart(sellDepthGridPane, "Âô¢Ù", 4, DepthSide.SELL, 0);
		getChildren().add(Borders.wrap(sellDepthGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).buildAll());

		GridPane latestPriceGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addLatestPricePart(latestPriceGridPane, "×îÐÂ");
		getChildren().add(Borders.wrap(latestPriceGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());

		GridPane buyDepthGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addMarketDepthPart(buyDepthGridPane, "Âò¢Ù", 0, DepthSide.BUY, 0);
		addMarketDepthPart(buyDepthGridPane, "Âò¢Ú", 1, DepthSide.BUY, 1);
		addMarketDepthPart(buyDepthGridPane, "Âò¢Û", 2, DepthSide.BUY, 2);
		addMarketDepthPart(buyDepthGridPane, "Âò¢Ü", 3, DepthSide.BUY, 3);
		addMarketDepthPart(buyDepthGridPane, "Âò¢Ý", 4, DepthSide.BUY, 4);
		getChildren().add(Borders.wrap(buyDepthGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).buildAll());
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

	private void addMarketDepthPart(GridPane gridPane, String name, int rowNum, DepthSide depthSide, int depth) {
		LabelToggleButton nameLabel = createLabelToggleButton(name);
		bindMarketDepth(depthSide, depth, nameLabel);
		gridPane.add(nameLabel, 0, rowNum);
		DoubleValueToggleButton priceLabel = createValueToggleButton();
		gridPane.add(priceLabel, 1, rowNum);
		addDepthListener(priceLabel, depthSide, depth, DepthType.PRICE);
		Label label = new Label("--");
		addDepthListener(label, depthSide, depth, DepthType.VOLUME);
		gridPane.add(label, 2, rowNum);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
	}

	private void bindMarketDepth(DepthSide depthSide, int depth, LabelToggleButton nameLabel) {
		if(depthSide== DepthSide.SELL){
			nameLabel.bind(marketData.askPrice(depth));
		}else{
			nameLabel.bind(marketData.bidPrice(depth));
		}
	}

	private void addLatestPricePart(GridPane gridPane, String name) {
		LabelToggleButton nameLabel = createLabelToggleButton(name);
		nameLabel.bind(marketData.latestPriceProperty());
		gridPane.add(nameLabel, 0, 0);
		DoubleValueToggleButton priceLabel = createValueToggleButton();
		gridPane.add(priceLabel, 1, 0);
		addMarketPriceListener(marketData.latestPriceProperty(), priceLabel);
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

	private void addDepthListener(final Labeled priceLabel, DepthSide depthSide, int depth, DepthType depthType) {
		if (depthType == DepthType.PRICE) {
			if (depthSide == DepthSide.SELL) {
				addMarketPriceListener(marketData.askPrice(depth), (DoubleValueToggleButton) priceLabel);
			} else {
				addMarketPriceListener(marketData.bidPrice(depth), (DoubleValueToggleButton) priceLabel);
			}
		} else {
			if (depthSide == DepthSide.SELL) {
				addMarketVolumeListener(priceLabel, depthType, marketData.askVolume(depth));
			} else {
				addMarketVolumeListener(priceLabel, depthType, marketData.bidVolume(depth));
			}
		}
	}

	private void addMarketVolumeListener(final Labeled priceLabel, final DepthType depthType, SimpleIntegerProperty simpleIntegerProperty) {
		simpleIntegerProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				priceLabel.setText(number2.toString());

			}
		});
	}

	private void addMarketPriceListener(SimpleDoubleProperty simpleDoubleProperty, final DoubleValueToggleButton priceLabel) {
		simpleDoubleProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				priceLabel.setText(number2.toString());
				priceLabel.setValue(number2.doubleValue());
				changePriceLabelStyle(priceLabel, marketData.getClosePrice(), number2.doubleValue());
				symbolTextField.setText("" + simpleDoubleProperty.get());
			}
		});
	}

	private LabelToggleButton createLabelToggleButton(String name) {
		LabelToggleButton toggleButton = new LabelToggleButton(name);
		toggleButton.getStyleClass().add("market-data-button");
		return toggleButton;
	}

	private DoubleValueToggleButton createValueToggleButton() {
		DoubleValueToggleButton toggleButton = new DoubleValueToggleButton("--");
		toggleButton.getStyleClass().add("market-data-button");
		priceButtons.add(toggleButton);
		return toggleButton;
	}

	private void changePriceLabelStyle(Labeled priceLabel, double closePrice, double price) {
		int result = Double.compare(closePrice, price);
		switch (result) {
			case 1:
				priceLabel.setStyle("-fx-text-fill: GREEN");
				break;
			case -1:
				priceLabel.setStyle("-fx-text-fill: RED");
				break;
			case 0:
				priceLabel.setStyle("");
				break;
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

	class DoubleValueToggleButton extends ToggleButton {

		private double value;

		DoubleValueToggleButton(String s) {
			super(s);
			this.value = 0.0;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
	}

	class LabelToggleButton extends ToggleButton {

		private SimpleDoubleProperty value = new SimpleDoubleProperty();

		LabelToggleButton(String s) {
			super(s);
		}

		public void bind(SimpleDoubleProperty marketdata){
			value.bind(marketdata);
		}

		public double getValue() {
			return value.get();
		}

		public SimpleDoubleProperty valueProperty() {
			return value;
		}
	}
}
