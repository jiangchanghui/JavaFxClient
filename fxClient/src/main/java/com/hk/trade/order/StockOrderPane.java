package com.hk.trade.order;

import com.hk.framework.ui.titledwindow.TitledWindow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by jiangch on 2014/6/27.
 */
public class StockOrderPane extends VBox {

	private TableViewWithColumn fundAssetTableView = new TableViewWithColumn(FundAsset.class);
	private TableViewWithColumn orderTableView = new TableViewWithColumn(Order.class);
	private Pane stockOrderMain ;

	public StockOrderPane(){
		super();
		TitledWindow fundAssetWindow = new TitledWindow(this);
		fundAssetWindow.setTitleName("基金资产");
		fundAssetTableView.setMinHeight(80);
		fundAssetWindow.setContent(fundAssetTableView);
		getChildren().add(fundAssetWindow);
		setVgrow(fundAssetWindow, Priority.SOMETIMES);

		TitledWindow stockOrderMainWindow = new TitledWindow(this);
		stockOrderMainWindow.setTitleName("下指令");
		stockOrderMain = StockOrderController.getInstance();
		stockOrderMainWindow.setContent(stockOrderMain);
		getChildren().addAll(stockOrderMainWindow);
		setVgrow(stockOrderMainWindow, Priority.ALWAYS);

		TitledWindow orderTableViewWindow = new TitledWindow(this);
		orderTableViewWindow.setTitleName("指令列表");
		orderTableView.setMinHeight(80);
		orderTableViewWindow.setContent(orderTableView);
		getChildren().add(orderTableViewWindow);
	}

}
