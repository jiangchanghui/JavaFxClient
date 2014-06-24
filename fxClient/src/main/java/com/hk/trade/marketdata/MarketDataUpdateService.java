package com.hk.trade.marketdata;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by jiangch on 2014/6/24.
 */
public class MarketDataUpdateService extends ScheduledService<ObservableList<MarketData>> {
	private MarketData marketData = new MarketData();
	private Random random = new Random(System.currentTimeMillis());
	private StringProperty selectedSymbol = new SimpleStringProperty();

	@Override
	protected Task<ObservableList<MarketData>> createTask() {
		return new UpdateTask();
	}

	public String getSelectedSymbol() {
		return selectedSymbol.get();
	}

	public StringProperty selectedSymbolProperty() {
		return selectedSymbol;
	}

	class UpdateTask extends Task<ObservableList<MarketData>> {

		@Override
		protected ObservableList<MarketData> call() throws Exception {
			System.out.println(getSelectedSymbol());
			updateMarketData();
			ObservableList<MarketData> marketDatas = FXCollections.observableArrayList();
			marketDatas.add(marketData);
			return marketDatas;
		}

		private void updateMarketData() {
			marketData.setClosePrice(nextDouble());
			marketData.setOpenPrice(nextDouble());
			marketData.setDailyDownLimitPrice(nextDouble());
			marketData.setDailyUpLimitPrice(nextDouble());
			marketData.setLatestPrice(nextDouble());
			for (int i = 0; i < 5; i++) {
				marketData.setAskPrice(i, nextDouble());
				marketData.setAskVolume(i, nextInt());
				marketData.setBidPrice(i, nextDouble());
				marketData.setBidVolume(i, nextInt());
			}
			marketData.setPriceDelta(BigDecimal.valueOf(marketData.getLatestPrice()-marketData.getClosePrice()).setScale(3,BigDecimal.ROUND_DOWN).doubleValue());
		}

		private int nextInt() {
			int i = random.nextInt();
			return Math.abs(i)%10000;
		}

		private double nextDouble() {
			BigDecimal bigDecimal = new BigDecimal(random.nextDouble());
			return bigDecimal.multiply(BigDecimal.valueOf(100)).setScale(3, BigDecimal.ROUND_DOWN).doubleValue();

		}

	}
}
