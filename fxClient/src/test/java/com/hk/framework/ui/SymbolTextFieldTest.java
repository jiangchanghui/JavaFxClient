package com.hk.framework.ui;

import com.hk.trade.marketdata.SecurityData;
import com.hk.trade.marketdata.SymbolTextField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by jiangch on 2014/6/24.
 */
public class SymbolTextFieldTest extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		VBox vBox = new VBox();
		ArrayList<SecurityData> possibleSecurities = new ArrayList<>();
		possibleSecurities.add(new SecurityData("600000","浦发银行"));
		possibleSecurities.add(new SecurityData("000001","深发展"));

		vBox.getChildren().add(new SymbolTextField(possibleSecurities));
		VBox e = new VBox();
		e.setMinHeight(200);
		vBox.getChildren().add(e);
		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
