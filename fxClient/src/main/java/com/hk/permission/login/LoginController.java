package com.hk.permission.login;

import com.hk.framework.JavaFxController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/7.
 */
public class LoginController extends JavaFxController {

    @FXML
    private TextField accountNo;
    @FXML
    private PasswordField accountPassword;
    @FXML
    private Label loginErrorMessage;
	private Stage stage;

	public void login(ActionEvent actionEvent) {
	    application.loadUI("mainframe");
		stage.hide();
		application.getPrimaryStage().show();
		application.getPrimaryStage().setMaximized(true);
//        try {
//            String account = accountNo.getText();
//            String password = accountPassword.getText();
//            Ticket tick = LoginCommandController.login(account, password);
//            SessionCache.setUserId(account);
//            String sessionId = tick.getSessionId();
//            SessionCache.setSessionId(sessionId);
//            SessionCache.setEnvironment(tick.getEnvironment());
//            SessionCache.setPermissions(tick.getPermissions());
//            application.loadMainFramework("main");
//            application.getPrimaryStage().setMaximized(true);
//        } catch (Exception e) {
//            Dialogs dialogs = Dialogs.create();
//            dialogs.message(e.getMessage());
//            dialogs.showError();
//        }

    }

    public void logout(ActionEvent actionEvent) {
        application.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginErrorMessage.setText("");
    }

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
}
