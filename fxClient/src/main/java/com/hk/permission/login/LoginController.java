package com.hk.permission.login;

import com.hk.framework.JavaFxController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
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
	public ProgressBar progressBar;
	@FXML
	public Label progressLabel;
	@FXML
	private CustomTextField accountNoTextField;
	@FXML
	private CustomPasswordField accountPasswordField;
	@FXML
	private Label loginErrorMessage;
	private Stage stage;
	private ValidationSupport validationSupport;
	private LoginService loginService = new LoginService();

	public void login(ActionEvent actionEvent) {
		String account = accountNoTextField.getText();
		String password = accountPasswordField.getText();
		loginService.setAccountInfo(new AccountInfo(account, password));
		if (loginService.stateProperty().get() == Worker.State.READY) {
			loginService.start();
		} else if (loginService.stateProperty().get() == Worker.State.RUNNING) {
			loginService.cancel();
			loginService.reset();
		} else {
			loginService.restart();
		}
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
		progressBar.setMaxSize(150, 150);
		progressBar.visibleProperty().bind(loginService.runningProperty());
		progressBar.progressProperty().bind(loginService.progressProperty());
		progressLabel.textProperty().bind(loginService.messageProperty());
		loginService.stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State state2) {
				if (Worker.State.RUNNING == state2) {
					loginButton.setText("»°  œ˚");
				} else if (Worker.State.SUCCEEDED == state2) {
					redirectToMain();
				} else {
					loginButton.setText("µ«  ¬Ω");
				}
			}
		});
		loginService.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observableValue, Throwable throwable, Throwable throwable2) {
				if(throwable2==null){
					loginErrorMessage.setText("");
				}else {
					loginErrorMessage.setText(throwable2.getMessage());
				}
			}
		});
		loginService.valueProperty().addListener(new ChangeListener<ObservableList<LoginResult>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<LoginResult>> observableValue, ObservableList<LoginResult> loginResults, ObservableList<LoginResult> loginResults2) {
				SessionManger.setCurrentAccount(loginResults2.get(0).getAccountNo());
			}
		});
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
}
