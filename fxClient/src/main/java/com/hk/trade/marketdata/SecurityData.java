package com.hk.trade.marketdata;

/**
 * Created by jiangch on 2014/6/24.
 */
public class SecurityData {
	private String symbol;
	private String name;

	public SecurityData(String symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%-6s%8s",symbol,name);
	}
}
