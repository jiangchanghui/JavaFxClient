package com.hk.permission.login;

import com.hk.framework.JavaFxController;

import com.hk.proxy.LoginCommandController;
import com.hk.proxy.amqp.SessionCache;
import elf.api.security.command.login.Ticket;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/7.
 */
public class LoginController extends JavaFxController {

	@FXML
	public Button loginButton;
	@FXML
	public Button settingButton;
	@FXML
    private CustomTextField accountNoTextField;
    @FXML
    private CustomPasswordField accountPasswordField;
    @FXML
    private Label loginErrorMessage;
	private Stage stage;
	private ValidationSupport validationSupport;

	public void login(ActionEvent actionEvent) {
        try {
            String account = accountNoTextField.getText();
	        String password = accountPasswordField.getText();
	        if(mockUser(account)){
		        redirectToMain();
		        SessionManger.setCurrentAccount(account);
	        }else {
		        authenticate(account, password);
		        redirectToMain();
	        }
        } catch (Exception e) {
	        loginErrorMessage.setText(e.getMessage());
        }
    }

	private void authenticate(String account, String password) {
		Ticket tick = LoginCommandController.login(account, password);
		SessionCache.setUserId(account);
		String sessionId = tick.getSessionId();
		SessionCache.setSessionId(sessionId);
		SessionCache.setEnvironment(tick.getEnvironment());
		SessionCache.setPermissions(tick.getPermissions());
		SessionManger.setCurrentAccount(account);
	}

	private boolean mockUser(String account) {
		return "admin".equalsIgnoreCase(account);
	}

	private void redirectToMain() {
		application.loadUI("mainframe");
		stage.close();
		application.getPrimaryStage().show();
		application.getPrimaryStage().setMaximized(true);
	}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginErrorMessage.setText("");
	    loginErrorMessage.setTextFill(Color.RED);
	    accountNoTextField.setPromptText("’À∫≈");
	    accountNoTextField.setLeft(new ImageView("com/hk/permission/login/user.png"));
	    accountPasswordField.setPromptText("√‹¬Î");
	    accountPasswordField.setLeft(new ImageView("com/hk/permission/login/lock.png"));
	    loginButton.setDefaultButton(true);
	    validationSupport = new ValidationSupport();
	    Platform.runLater(() -> {
		    String requiredFormat = "«Î ‰»Î'%s'";
		    validationSupport.registerValidator(accountNoTextField, Validator.createEmptyValidator(String.format(requiredFormat, "’À∫≈")));
		    validationSupport.registerValidator(accountPasswordField, Validator.createEmptyValidator(String.format(requiredFormat, "√‹¬Î")));
		    validationSupport.invalidProperty().addListener(new ChangeListener<Boolean>() {
			    @Override
			    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
				    loginButton.setDisable(aBoolean2);
			    }
		    });
		    accountNoTextField.requestFocus();
	    });
    }

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
}
