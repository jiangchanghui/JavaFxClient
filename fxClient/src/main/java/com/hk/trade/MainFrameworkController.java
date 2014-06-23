package com.hk.trade;

import com.hk.framework.JavaFxController;
import com.hk.permission.login.SessionManger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

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

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		userNameLabel.setText(SessionManger.getCurrentAccount());
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(stockTradingButton,bondTradingButton,futureTradingButton);
		toggleGroup.selectToggle(stockTradingButton);
	}
}
