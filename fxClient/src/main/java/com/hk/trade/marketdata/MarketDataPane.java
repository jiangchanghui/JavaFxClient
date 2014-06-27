package com.hk.trade.marketdata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.tools.Borders;

import java.util.ArrayList;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MarketDataPane extends VBox {
	private ToggleGroup toggleGroup = new ToggleGroup();
	private SymbolTextField symbolTextField;
	private MarketData marketData = new MarketData();
	private MarketDataUpdateService marketDataUpdateService = new MarketDataUpdateService();
	private SimpleDoubleProperty selectedValue = new SimpleDoubleProperty();

	public MarketDataPane() {
		super();
		addCss();
		initSymbolInputPart();
		initMarketDepthPart();
		initStaticMarketDataPart();
		initUpdateService();
		initSelectedValue();
	}

	private void initSelectedValue() {
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle2) {
				if (toggle2 instanceof PriceToggleButton) {
					selectedValue.unbind();
					selectedValue.set(((PriceToggleButton) toggle2).getPrice());
				} else if (toggle2 instanceof TagToggleButton) {
					selectedValue.unbind();
					selectedValue.bind(((TagToggleButton) toggle2).priceProperty());
				}
			}
		});
	}

	public SimpleDoubleProperty selectedValueProperty() {
		return selectedValue;
	}

	private void addCss() {
		getStylesheets().add("com/hk/trade/marketdata/marketdata.css");
	}

	private void initUpdateService() {
		marketDataUpdateService.valueProperty().addListener(new ChangeListener<ObservableList<MarketData>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<MarketData>> observableValue, ObservableList<MarketData> marketDatas, ObservableList<MarketData> marketDatas2) {
				if (marketDatas2 != null) {
					marketData.update(marketDatas2.get(0));
				}
			}
		});
		marketDataUpdateService.selectedSymbolProperty().bind(symbolTextField.selectedSymbolProperty());
		marketDataUpdateService.setPeriod(Duration.seconds(5));
	}

	private void initSymbolInputPart() {
		ArrayList<SecurityData> possibleSecurities = new ArrayList<>();
		possibleSecurities.add(new SecurityData("600000", "浦发银行"));
		possibleSecurities.add(new SecurityData("000001", "深发展"));
		symbolTextField = new SymbolTextField(possibleSecurities);
		getChildren().add(symbolTextField);
	}

	private void initStaticMarketDataPart() {
		ColumnConstraints titleColumn = getColumnConstraints(40,HPos.LEFT);
		ColumnConstraints valueColumn = getColumnConstraints(80,HPos.RIGHT);
		ColumnConstraints valueColumn2 = getColumnConstraints(60,HPos.RIGHT);
		addDailyLimitPart(titleColumn, valueColumn, titleColumn, valueColumn2);
		addOpenClosePricePart(titleColumn, valueColumn, titleColumn, valueColumn2);
	}

	private void addOpenClosePricePart(ColumnConstraints titleColumn, ColumnConstraints valueColumn, ColumnConstraints titleColumn2, ColumnConstraints valueColumn2) {
		GridPane openClosePriceGridPane = new GridPane();
		openClosePriceGridPane.setHgap(2);
		openClosePriceGridPane.getColumnConstraints().addAll(titleColumn, valueColumn, titleColumn2, valueColumn2);
		TagToggleButton closePriceLabel = new TagToggleButton("昨收");
		closePriceLabel.priceProperty().bind(marketData.closePriceProperty());
		openClosePriceGridPane.add(closePriceLabel, 0, 0);
		PriceToggleButton closePriceValue = new PriceToggleButton();
		openClosePriceGridPane.add(closePriceValue, 1, 0);
		bindMarketPrice(closePriceValue, marketData.closePriceProperty(), marketData.closePriceProperty());
		TagToggleButton openPriceLabel = new TagToggleButton("今开");
		openPriceLabel.priceProperty().bind(marketData.openPriceProperty());
		openClosePriceGridPane.add(openPriceLabel, 2, 0);
		PriceToggleButton openPriceValue = new PriceToggleButton();
		bindMarketPrice(openPriceValue, marketData.openPriceProperty(), marketData.closePriceProperty());
		openClosePriceGridPane.add(openPriceValue, 3, 0);
		toggleGroup.getToggles().addAll(closePriceLabel, closePriceValue, openPriceLabel, openPriceValue);
		getChildren().add(Borders.wrap(openClosePriceGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 1, 1).buildAll());
	}

	private void addDailyLimitPart(ColumnConstraints titleColumn, ColumnConstraints valueColumn, ColumnConstraints titleColumn2, ColumnConstraints valueColumn2) {
		GridPane dailyLimitGridPane = new GridPane();
		dailyLimitGridPane.setHgap(2);
		dailyLimitGridPane.getColumnConstraints().addAll(titleColumn, valueColumn, titleColumn2, valueColumn2);
		TagToggleButton nameLabel = new TagToggleButton("涨停");
		nameLabel.priceProperty().bind(marketData.dailyUpLimitPriceProperty());
		dailyLimitGridPane.add(nameLabel, 0, 0);
		PriceToggleButton priceLabel = new PriceToggleButton();
		bindMarketPrice(priceLabel, marketData.dailyUpLimitPriceProperty(), marketData.closePriceProperty());
		dailyLimitGridPane.add(priceLabel, 1, 0);

		TagToggleButton nameLabel2 = new TagToggleButton("跌停");
		nameLabel2.priceProperty().bind(marketData.dailyDownLimitPriceProperty());
		dailyLimitGridPane.add(nameLabel2, 2, 0);
		PriceToggleButton priceLabel2 = new PriceToggleButton();
		bindMarketPrice(priceLabel2, marketData.dailyDownLimitPriceProperty(), marketData.closePriceProperty());

		dailyLimitGridPane.add(priceLabel2, 3, 0);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
		toggleGroup.getToggles().addAll(nameLabel2, priceLabel2);
		getChildren().add(Borders.wrap(dailyLimitGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());
	}

	private void initMarketDepthPart() {
		ColumnConstraints firstColumn = getColumnConstraints(40,HPos.LEFT);
		ColumnConstraints secondColumn = getColumnConstraints(80,HPos.RIGHT);
		ColumnConstraints lastColumn = getColumnConstraints(100,HPos.RIGHT);

		GridPane sellDepthGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addMarketDepthPart(sellDepthGridPane, "卖⑤", 0, DepthSide.SELL, 4);
		addMarketDepthPart(sellDepthGridPane, "卖④", 1, DepthSide.SELL, 3);
		addMarketDepthPart(sellDepthGridPane, "卖③", 2, DepthSide.SELL, 2);
		addMarketDepthPart(sellDepthGridPane, "卖②", 3, DepthSide.SELL, 1);
		addMarketDepthPart(sellDepthGridPane, "卖①", 4, DepthSide.SELL, 0);
		getChildren().add(Borders.wrap(sellDepthGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).buildAll());

		GridPane latestPriceGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addLatestPricePart(latestPriceGridPane, "最新");
		getChildren().add(Borders.wrap(latestPriceGridPane).lineBorder().color(Color.GRAY).innerPadding(0).outerPadding(0).thickness(0, 1, 0, 1).buildAll());

		GridPane buyDepthGridPane = getGridPane(firstColumn, secondColumn, lastColumn);
		addMarketDepthPart(buyDepthGridPane, "买①", 0, DepthSide.BUY, 0);
		addMarketDepthPart(buyDepthGridPane, "买②", 1, DepthSide.BUY, 1);
		addMarketDepthPart(buyDepthGridPane, "买③", 2, DepthSide.BUY, 2);
		addMarketDepthPart(buyDepthGridPane, "买④", 3, DepthSide.BUY, 3);
		addMarketDepthPart(buyDepthGridPane, "买⑤", 4, DepthSide.BUY, 4);
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

	private ColumnConstraints getColumnConstraints(int minWidth,HPos pos) {
		ColumnConstraints lastColumn = new ColumnConstraints();
		lastColumn.setMinWidth(minWidth);
		lastColumn.setHgrow(Priority.SOMETIMES);
		lastColumn.setHalignment(pos);
		return lastColumn;
	}

	private void addMarketDepthPart(GridPane gridPane, String name, int rowNum, DepthSide depthSide, int depth) {
		TagToggleButton nameLabel = new TagToggleButton(name);
		bindMarketDepth(depthSide, depth, nameLabel);
		gridPane.add(nameLabel, 0, rowNum);
		PriceToggleButton priceLabel = new PriceToggleButton();
		gridPane.add(priceLabel, 1, rowNum);
		bindMarketDepthPrice(priceLabel, depthSide, depth);
		VolumeLabel label = new VolumeLabel(depthSide);
		bindMarketDepthVolume(label, depthSide, depth);
		gridPane.add(label, 2, rowNum);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
	}



	private void addLatestPricePart(GridPane gridPane, String name) {
		TagToggleButton nameLabel = new TagToggleButton(name);
		nameLabel.priceProperty().bind(marketData.latestPriceProperty());
		gridPane.add(nameLabel, 0, 0);
		PriceToggleButton priceLabel = new PriceToggleButton();
		gridPane.add(priceLabel, 1, 0);
		bindMarketPrice(priceLabel, marketData.latestPriceProperty(), marketData.closePriceProperty());
		Label label1 = new Label("--");
		label1.setAlignment(Pos.CENTER_RIGHT);
		marketData.priceDeltaProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				label1.setText(PriceToggleButton.formatText(number2.doubleValue()));
			}
		});
		gridPane.add(label1, 2, 0);
		toggleGroup.getToggles().addAll(nameLabel, priceLabel);
		nameLabel.styleProperty().bind(priceLabel.styleProperty());
		label1.styleProperty().bind(priceLabel.styleProperty());
	}

	private void bindMarketDepthVolume(VolumeLabel priceLabel, DepthSide depthSide, int depth) {
		if (depthSide == DepthSide.SELL) {
			bindMarketVolume(priceLabel, marketData.askVolume(depth), marketData.maxAskVolumeProperty());
		} else {
			bindMarketVolume(priceLabel, marketData.bidVolume(depth), marketData.maxBidVolumeProperty());
		}
	}

	private void bindMarketDepth(DepthSide depthSide, int depth, TagToggleButton nameLabel) {
		if (depthSide == DepthSide.SELL) {
			nameLabel.priceProperty().bind(marketData.askPrice(depth));
		} else {
			nameLabel.priceProperty().bind(marketData.bidPrice(depth));
		}
	}

	private void bindMarketDepthPrice(PriceToggleButton priceLabel, DepthSide depthSide, int depth) {
		if (depthSide == DepthSide.SELL) {
			bindMarketPrice(priceLabel, marketData.askPrice(depth), marketData.closePriceProperty());
		} else {
			bindMarketPrice(priceLabel, marketData.bidPrice(depth), marketData.closePriceProperty());
		}
	}

	private void bindMarketVolume(final VolumeLabel priceLabel, SimpleIntegerProperty value, SimpleIntegerProperty max) {
		priceLabel.valueProperty().bind(value);
		priceLabel.maxProperty().bind(max);
	}

	private void bindMarketPrice(PriceToggleButton priceToggleButton, SimpleDoubleProperty price, SimpleDoubleProperty closePrice) {
		priceToggleButton.priceProperty().bind(price);
		priceToggleButton.closePriceProperty().bind(closePrice);
	}

	enum DepthSide {
		SELL, BUY
	}
}
