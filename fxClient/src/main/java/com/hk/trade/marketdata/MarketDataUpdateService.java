package com.hk.trade.marketdata;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
			if(StringUtils.isEmpty(getSelectedSymbol())){
				ObservableList<MarketData> marketDatas = FXCollections.observableArrayList();
				marketDatas.add(new MarketData());
				return marketDatas;
			}
			updateMarketData();
			ObservableList<MarketData> marketDatas = FXCollections.observableArrayList();
			marketDatas.add(marketData);
			return marketDatas;
		}

		private void updateMarketData() {

			List<Integer> tmpIntVolume = new ArrayList<>(10);
			List<Double> tmpDoubleVolume = new ArrayList<>(10);
			for (int i = 0; i < 10; i++) {
				tmpIntVolume.add(nextInt());
				tmpDoubleVolume.add(nextDouble());

			}
			tmpDoubleVolume.sort(new Comparator<Double>() {
				@Override
				public int compare(Double o1, Double o2) {
					return Double.compare(o1,o2);
				}
			});
			for(int i=0;i<5;i++){
				marketData.setAskPrice(i, tmpDoubleVolume.get(i+5));
				marketData.setAskVolume(i, nextInt());
				marketData.setBidPrice(4-i, tmpDoubleVolume.get(i));
				marketData.setBidVolume(i, nextInt());
			}
			marketData.setLatestPrice(tmpDoubleVolume.get(5)-0.0001);
			marketData.setClosePrice(BigDecimal.valueOf(tmpDoubleVolume.get(4)+random.nextDouble()).setScale(3,BigDecimal.ROUND_CEILING).doubleValue());
			marketData.setOpenPrice(BigDecimal.valueOf(tmpDoubleVolume.get(4)+random.nextDouble()).setScale(3,BigDecimal.ROUND_CEILING).doubleValue());
			marketData.setDailyDownLimitPrice(marketData.getClosePrice()*0.9);
			marketData.setDailyUpLimitPrice(marketData.getClosePrice()*1.1);
			marketData.setPriceDelta(BigDecimal.valueOf(marketData.getLatestPrice() - marketData.getClosePrice()).setScale(3, BigDecimal.ROUND_DOWN).doubleValue());
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
