package com.hk.login;

import com.hk.framework.FxmlContent;
import com.hk.framework.FxmlLoadUtils;
import com.hk.framework.ui.undecorator.Undecorator;
import com.hk.framework.ui.undecorator.UndecoratorScene;
import com.hk.remote.amqp.RabbitConfiguration;
import com.hk.remote.amqp.RabbitMethodInterceptor;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/7.
 */
public class LoginController extends Pane implements Initializable {

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
	private ValidationSupport validationSupport;
	private LoginService loginService = new LoginService();
	private SimpleBooleanProperty loginState = new SimpleBooleanProperty(false);
	private Stage stage;

	public static FxmlContent getInstance() {
		FxmlContent content = FxmlLoadUtils.loadFxml("/com/hk/login/Login.fxml");
		return content;
	}

	public static LoginController loadLoginUI(Stage owner) {
		Stage loginStage = new Stage();
		loginStage.setResizable(false);
		FxmlContent content = getInstance();
		final UndecoratorScene undecoratorScene = new UndecoratorScene(loginStage, content.getPane());
		undecoratorScene.setFadeInTransition();
		loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				we.consume();   // Do not hide
				((LoginController) (content.getController())).close();
				undecoratorScene.setFadeOutTransition();
				RabbitConfiguration.destroy();
				RabbitMethodInterceptor.destroy();
				owner.close();
			}
		});
		loginStage.initOwner(owner);
		loginStage.setScene(undecoratorScene);
		loginStage.sizeToScene();
		loginStage.toFront();
		Undecorator undecorator = undecoratorScene.getUndecorator();
		loginStage.setMinWidth(undecorator.getMinWidth());
		loginStage.setMinHeight(undecorator.getMinHeight());
		loginStage.show();
		LoginController controller = (LoginController) content.getController();
		controller.setStage(loginStage);
		return controller;
	}

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

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		loginErrorMessage.setText("");
		loginErrorMessage.setTextFill(Color.RED);
		accountNoTextField.setPromptText("’À∫≈");
		accountNoTextField.setLeft(new ImageView("com/hk/login/user.png"));
		accountPasswordField.setPromptText("√‹¬Î");
		accountPasswordField.setLeft(new ImageView("com/hk/login/lock.png"));
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
					loginState.set(true);
					close();
				} else {
					loginButton.setText("µ«  ¬Ω");
				}
			}
		});
		loginService.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observableValue, Throwable throwable, Throwable throwable2) {
				if (throwable2 == null) {
					loginErrorMessage.setText("");
				} else {
					loginErrorMessage.setText(throwable2.getMessage());
				}
			}
		});
		loginService.valueProperty().addListener(new ChangeListener<ObservableList<LoginResult>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<LoginResult>> observableValue, ObservableList<LoginResult> loginResults, ObservableList<LoginResult> loginResults2) {
				if (loginResults2 == null) {
					return;
				}
				SessionManger.setCurrentAccount(loginResults2.get(0).getAccountNo());
			}
		});

	}

	public SimpleBooleanProperty loginStateProperty() {
		return loginState;
	}

	public void close() {
		loginService.cancel();
		loginService.reset();
		this.stage.close();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}
}
