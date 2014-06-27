package com.hk.trade.order;

import com.hk.framework.ui.tableview.TableColumnName;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by jiangch on 2014/6/27.
 */
public class Order {

	@TableColumnName("���")
	private SimpleStringProperty orderId = new SimpleStringProperty();
	@TableColumnName("֤ȯ����")
	private SimpleStringProperty symbol = new SimpleStringProperty();
	@TableColumnName("֤ȯ����")
	private SimpleStringProperty securityName = new SimpleStringProperty();
	@TableColumnName("Ŀ������")
	private SimpleStringProperty targetType = new SimpleStringProperty();
	@TableColumnName("����")
	private SimpleIntegerProperty quantity = new SimpleIntegerProperty();
	@TableColumnName("���")
	private SimpleDoubleProperty amount = new SimpleDoubleProperty();
	@TableColumnName("�۸�")
	private SimpleDoubleProperty price = new SimpleDoubleProperty();

	public String getOrderId() {
		return orderId.get();
	}

	public SimpleStringProperty orderIdProperty() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId.set(orderId);
	}

	public String getSymbol() {
		return symbol.get();
	}

	public SimpleStringProperty symbolProperty() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol.set(symbol);
	}

	public String getSecurityName() {
		return securityName.get();
	}

	public SimpleStringProperty securityNameProperty() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName.set(securityName);
	}

	public String getTargetType() {
		return targetType.get();
	}

	public SimpleStringProperty targetTypeProperty() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType.set(targetType);
	}

	public int getQuantity() {
		return quantity.get();
	}

	public SimpleIntegerProperty quantityProperty() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}

	public double getAmount() {
		return amount.get();
	}

	public SimpleDoubleProperty amountProperty() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount.set(amount);
	}

	public double getPrice() {
		return price.get();
	}

	public SimpleDoubleProperty priceProperty() {
		return price;
	}

	public void setPrice(double price) {
		this.price.set(price);
	}
}
