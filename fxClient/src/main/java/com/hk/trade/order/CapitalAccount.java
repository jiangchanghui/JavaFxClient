package com.hk.trade.order;

import com.hk.framework.ui.tableview.TableColumnName;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by jiangch on 2014/6/26.
 */
public class CapitalAccount {
	@TableColumnName("基金")
	private SimpleStringProperty fundName = new SimpleStringProperty();
	@TableColumnName("组合")
	private SimpleStringProperty portfolioName = new SimpleStringProperty();
	@TableColumnName(value = "账户名称")
	private SimpleStringProperty accountName = new SimpleStringProperty();
	@TableColumnName("资金账号")
	private SimpleStringProperty accountNo = new SimpleStringProperty();
	@TableColumnName("可用资金")
	private SimpleDoubleProperty availableAmount = new SimpleDoubleProperty();
	@TableColumnName("可买量")
	private SimpleIntegerProperty quantityToBuy = new SimpleIntegerProperty();
	@TableColumnName("可卖量")
	private SimpleIntegerProperty quantityToSell = new SimpleIntegerProperty();
	public CapitalAccount(String accountNo,String accountName) {
		setAccountNo(accountNo);
		setAccountName(accountName);

	}
	public SimpleStringProperty accountNoProperty() {
		return accountNo;
	}

	public String getAccountNo() {
		return accountNo.get();
	}

	public void setAccountNo(String accountNo) {
		this.accountNo.set(accountNo);
	}

	public String getAccountName() {
		return accountName.get();
	}

	public SimpleStringProperty accountNameProperty() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName.set(accountName);
	}

	public String getFundName() {
		return fundName.get();
	}

	public SimpleStringProperty fundNameProperty() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName.set(fundName);
	}

	public String getPortfolioName() {
		return portfolioName.get();
	}

	public SimpleStringProperty portfolioNameProperty() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName.set(portfolioName);
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

	public int getQuantityToBuy() {
		return quantityToBuy.get();
	}

	public SimpleIntegerProperty quantityToBuyProperty() {
		return quantityToBuy;
	}

	public void setQuantityToBuy(int quantityToBuy) {
		this.quantityToBuy.set(quantityToBuy);
	}

	public int getQuantityToSell() {
		return quantityToSell.get();
	}

	public SimpleIntegerProperty quantityToSellProperty() {
		return quantityToSell;
	}

	public void setQuantityToSell(int quantityToSell) {
		this.quantityToSell.set(quantityToSell);
	}
}
