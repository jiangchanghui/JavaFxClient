package com.hk.trade;

import com.hk.framework.JavaFxController;
import com.hk.permission.login.SessionManger;
import com.hk.trade.order.StockOrderPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MainFrameworkController extends JavaFxController {
	@FXML
	public ToggleButton stockTradingButton;
	@FXML
	public ToggleButton bondTradingButton;
	@FXML
	public ToggleButton futureTradingButton;
	@FXML
	public Label userNameLabel;
	@FXML
	public VBox mainPane;

	private StockOrderPane stockOrderPane = new StockOrderPane();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		userNameLabel.setText(SessionManger.getCurrentAccount());
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(stockTradingButton, bondTradingButton, futureTradingButton);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle old, Toggle current) {
				System.out.println(((ToggleButton)current).getText());
				if(current == stockTradingButton){
					mainPane.getChildren().clear();
					mainPane.getChildren().add(stockOrderPane);
					VBox.setVgrow(stockOrderPane, Priority.ALWAYS);
				} else if(current == bondTradingButton){
					mainPane.getChildren().clear();
					mainPane.getChildren().add(new Button("债券交易"));
				} else{
					mainPane.getChildren().clear();
					mainPane.getChildren().add(new Button("期货交易"));
				}
			}
		});
		toggleGroup.selectToggle(stockTradingButton);

	}

}
