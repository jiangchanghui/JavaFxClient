package com.hk.query;

import com.hk.framework.ui.tableview.TableColumnName;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by jiangch on 2014/6/27.
 */
public class FundAsset {

	@TableColumnName("基金")
	private SimpleStringProperty fund = new SimpleStringProperty();
	@TableColumnName("可用金额")
	private SimpleDoubleProperty availableAmount = new SimpleDoubleProperty();
	@TableColumnName("股票资产")
	private SimpleDoubleProperty stockAsset = new SimpleDoubleProperty();

	public String getFund() {
		return fund.get();
	}

	public SimpleStringProperty fundProperty() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund.set(fund);
	}

	public double getAvailableAmount() {
		return availableAmount.get();
	}

	public SimpleDoubleProperty availableAmountProperty() {
		return availableAmount;
	}

	public void setAvailableAmount(double availableAmount) {
		this.availableAmount.set(availableAmount);
	}

	public double getStockAsset() {
		return stockAsset.get();
	}

	public SimpleDoubleProperty stockAssetProperty() {
		return stockAsset;
	}

	public void setStockAsset(double stockAsset) {
		this.stockAsset.set(stockAsset);
	}
}
