package com.hk.framework;

import com.hk.framework.ui.undecorator.Undecorator;
import com.hk.framework.ui.undecorator.UndecoratorScene;
import com.hk.permission.login.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public abstract class JavaFxApplication extends Application {

	protected Stage primaryStage;

	private String rootSceneName;
	private String applicationTitle;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setResizable(true);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		setUserAgentStylesheet(STYLESHEET_MODENA);
		loadLoginUI("../permission/login/Login");
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void close() {
		primaryStage.close();
	}

	private FxmlContent loadFxml(String fxmlUri) {
		URL fxml = getClass().getResource(fxmlUri + ".fxml");
		FXMLLoader loader = new FXMLLoader(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			Pane pane = (Pane) loader.load();
			JavaFxController controller = JavaFxController.class.cast(loader.getController()).setUp(this);
			return new FxmlContent(pane, controller);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public JavaFxController loadUI(String sceneName) {
		FxmlContent fxmlContent = loadFxml(sceneName);
		if (fxmlContent == null) {
			return null;
		}
		final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, fxmlContent.getPane());
		undecoratorScene.setFadeInTransition();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				we.consume();   // Do not hide
				undecoratorScene.setFadeOutTransition();
			}
		});
		primaryStage.setScene(undecoratorScene);
		primaryStage.sizeToScene();
		primaryStage.toFront();
		Undecorator undecorator = undecoratorScene.getUndecorator();
		primaryStage.setMinWidth(undecorator.getMinWidth());
		primaryStage.setMinHeight(undecorator.getMinHeight());
		return fxmlContent.getController();
	}


	public void loadLoginUI(String sceneName) {
		Stage stage = new Stage();
		FxmlContent fxmlContent = loadFxml(sceneName);
		if (fxmlContent == null) {
			return;
		}
		stage.setResizable(false);
		final UndecoratorScene undecoratorScene = new UndecoratorScene(stage, fxmlContent.getPane());
		undecoratorScene.setFadeInTransition();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				we.consume();   // Do not hide
				undecoratorScene.setFadeOutTransition();
				primaryStage.close();
			}
		});
		LoginController loginController = (LoginController) fxmlContent.getController();
		loginController.setStage(stage);
		stage.initOwner(primaryStage);
		stage.setScene(undecoratorScene);
		stage.sizeToScene();
		stage.toFront();
		Undecorator undecorator = undecoratorScene.getUndecorator();
		stage.setMinWidth(undecorator.getMinWidth());
		stage.setMinHeight(undecorator.getMinHeight());
		stage.show();
	}



}
