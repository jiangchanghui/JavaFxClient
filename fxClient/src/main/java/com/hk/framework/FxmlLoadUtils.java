package com.hk.framework;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jiangch on 2014/6/26.
 */
public class FxmlLoadUtils {

	public static Pane loadFxml(String fxmlUri, Class<?> classe) {
		URL fxml = classe.getResource(fxmlUri);
		System.out.println(fxml);
		FXMLLoader loader = new FXMLLoader(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			Pane pane = (Pane) loader.load();
			return pane;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
