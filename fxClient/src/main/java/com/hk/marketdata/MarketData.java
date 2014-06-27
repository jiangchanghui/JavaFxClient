package com.hk.marketdata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MarketData  {
	public static final int MAX_DEPTH = 5;
	private SimpleDoubleProperty latestPrice = new SimpleDoubleProperty();
	private SimpleDoubleProperty priceDelta = new SimpleDoubleProperty();
	private SimpleDoubleProperty openPrice = new SimpleDoubleProperty();
	private SimpleDoubleProperty closePrice = new SimpleDoubleProperty();
	private MarketDepth[] asks = new MarketDepth[MAX_DEPTH];
	private MarketDepth[] bids = new MarketDepth[MAX_DEPTH];
	private SimpleDoubleProperty dailyUpLimitPrice = new SimpleDoubleProperty();
	private SimpleDoubleProperty dailyDownLimitPrice = new SimpleDoubleProperty();
	private SimpleIntegerProperty maxAskVolume = new SimpleIntegerProperty(1);
	private SimpleIntegerProperty maxBidVolume = new SimpleIntegerProperty(1);

	public MarketData(){
		for (int i = 0; i < MAX_DEPTH; i++) {
			asks[i] = new MarketDepth();
			bids[i] = new MarketDepth();
		}
	}
	public double getOpenPrice() {
		return openPrice.get();
	}

	public SimpleDoubleProperty openPriceProperty() {
		return openPrice;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice.set(openPrice);
	}

	public double getClosePrice() {
		return closePrice.get();
	}

	public SimpleDoubleProperty closePriceProperty() {
		return closePrice;
	}

	public void setClosePrice(double closePrice) {
		this.closePrice.set(closePrice);
	}

	public double getAskPrice(int depth) {
		validate(depth);
		return asks[depth].getPrice();
	}

	public void setAskPrice(int depth, double price) {
		validate(depth);
		asks[depth].setPrice(price);
	}

	public SimpleDoubleProperty askPrice(int depth) {
		validate(depth);
		return asks[depth].priceProperty();
	}

	public int getAskVolume(int depth) {
		validate(depth);

		return asks[depth].getVolume();
	}

	public void setAskVolume(int depth, int volume) {
		validate(depth);
		if(volume>maxAskVolume.get()){
			maxAskVolume.set(volume);
		}
		asks[depth].setVolume(volume);
	}

	public SimpleIntegerProperty askVolume(int depth) {
		validate(depth);
		return asks[depth].volumeProperty();
	}

	public double getBidPrice(int depth) {
		validate(depth);
		return bids[depth].getPrice();
	}

	public void setBidPrice(int depth, double price) {
		validate(depth);
		bids[depth].setPrice(price);
	}

	public SimpleDoubleProperty bidPrice(int depth) {
		validate(depth);
		return bids[depth].priceProperty();
	}

	public int getBidVolume(int depth) {
		validate(depth);
		return bids[depth].getVolume();
	}

	public void setBidVolume(int depth, int volume) {
		validate(depth);
		if(volume>maxBidVolume.get()){
			maxBidVolume.set(volume);
		}
		bids[depth].setVolume(volume);
	}

	public SimpleIntegerProperty bidVolume(int depth) {
		validate(depth);
		return bids[depth].volumeProperty();
	}

	public double getLatestPrice() {
		return latestPrice.get();
	}

	public SimpleDoubleProperty latestPriceProperty() {
		return latestPrice;
	}

	public void setLatestPrice(double latestPrice) {
		this.latestPrice.set(latestPrice);
	}

	public double getPriceDelta() {
		return priceDelta.get();
	}

	public SimpleDoubleProperty priceDeltaProperty() {
		return priceDelta;
	}

	public void setPriceDelta(double priceDelta) {
		this.priceDelta.set(priceDelta);
	}

	public double getDailyUpLimitPrice() {
		return dailyUpLimitPrice.get();
	}

	public SimpleDoubleProperty dailyUpLimitPriceProperty() {
		return dailyUpLimitPrice;
	}

	public void setDailyUpLimitPrice(double dailyUpLimitPrice) {
		this.dailyUpLimitPrice.set(dailyUpLimitPrice);
	}

	public double getDailyDownLimitPrice() {
		return dailyDownLimitPrice.get();
	}

	public SimpleDoubleProperty dailyDownLimitPriceProperty() {
		return dailyDownLimitPrice;
	}

	public void setDailyDownLimitPrice(double dailyDownLimitPrice) {
		this.dailyDownLimitPrice.set(dailyDownLimitPrice);
	}

	public int getMaxAskVolume() {
		return maxAskVolume.get();
	}

	public SimpleIntegerProperty maxAskVolumeProperty() {
		return maxAskVolume;
	}

	public void setMaxAskVolume(int maxAskVolume) {
		this.maxAskVolume.set(maxAskVolume);
	}

	public int getMaxBidVolume() {
		return maxBidVolume.get();
	}

	public SimpleIntegerProperty maxBidVolumeProperty() {
		return maxBidVolume;
	}

	public void setMaxBidVolume(int maxBidVolume) {
		this.maxBidVolume.set(maxBidVolume);
	}

	private void validate(int depth) {
		if (depth < 0 || depth >= MAX_DEPTH) {
			throw new IllegalArgumentException("盘口行情请求参数不对");
		}
	}

	public void update(MarketData marketData){
		setLatestPrice(marketData.getLatestPrice());
		setDailyUpLimitPrice(marketData.getDailyUpLimitPrice());
		setDailyDownLimitPrice(marketData.getDailyDownLimitPrice());
		setOpenPrice(marketData.getOpenPrice());
		setClosePrice(marketData.getClosePrice());
		setPriceDelta(marketData.getPriceDelta());
		for(int i=0;i<MAX_DEPTH;i++){
			setAskPrice(i,marketData.getAskPrice(i));
			setAskVolume(i,marketData.getAskVolume(i));
			setBidPrice(i,marketData.getBidPrice(i));
			setBidVolume(i,marketData.getBidVolume(i));
		}
	}

}
