package com.hk.main;

import com.hk.framework.FxmlContent;
import com.hk.framework.FxmlLoadUtils;
import com.hk.framework.ui.undecorator.Undecorator;
import com.hk.framework.ui.undecorator.UndecoratorScene;
import com.hk.login.SessionManger;
import com.hk.remote.amqp.RabbitConfiguration;
import com.hk.remote.amqp.RabbitMethodInterceptor;
import com.hk.trade.order.StockOrderPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/23.
 */
public class MainFrameworkController implements Initializable {
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

	public static Initializable loadUI(Stage primaryStage) {
		FxmlContent fxmlContent = FxmlLoadUtils.loadFxml("/com/hk/main/mainframe.fxml");
		if (fxmlContent == null) {
			return null;
		}
		final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, fxmlContent.getPane());
		undecoratorScene.setFadeInTransition();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				undecoratorScene.setFadeOutTransition();
				RabbitConfiguration.destroy();
				RabbitMethodInterceptor.destroy();
				we.consume();   // Do not hide
			}
		});
		primaryStage.setScene(undecoratorScene);
		primaryStage.sizeToScene();
		primaryStage.toFront();
		Undecorator undecorator = undecoratorScene.getUndecorator();
		primaryStage.setMinWidth(undecorator.getMinWidth());
		primaryStage.setMinHeight(undecorator.getMinHeight());
		primaryStage.setMaximized(true);
		primaryStage.show();
		return (Initializable) fxmlContent.getController();
	}

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
