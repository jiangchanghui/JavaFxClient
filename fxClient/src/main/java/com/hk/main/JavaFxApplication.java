package com.hk.main;

import com.hk.login.LoginController;
import com.hk.main.MainFrameworkController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFxApplication extends Application {

	protected Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setResizable(true);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		setUserAgentStylesheet(STYLESHEET_MODENA);
		LoginController loginController = LoginController.loadLoginUI(primaryStage);
		redirectToMainWhenLogin(loginController);
	}

	private void redirectToMainWhenLogin(LoginController loginController) {
		loginController.loginStateProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue==true){
					MainFrameworkController.loadUI(primaryStage);
				}
			}
		});
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
