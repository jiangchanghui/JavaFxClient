package com.hk.trade.order.query;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Created by jiangch on 2014/6/15.
 */
public class PortfolioQueryResult implements Serializable {

    private SimpleStringProperty portfolioCode =new SimpleStringProperty();
    private SimpleStringProperty portfolioName = new SimpleStringProperty();

    public String getPortfolioCode() {
        return portfolioCode.get();
    }

    public SimpleStringProperty portfolioCodeProperty() {
        return portfolioCode;
    }

    public void setPortfolioCode(String portfolioCode) {
        this.portfolioCode.set(portfolioCode);
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
}
