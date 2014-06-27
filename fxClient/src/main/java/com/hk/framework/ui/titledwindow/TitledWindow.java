package com.hk.framework.ui.titledwindow;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Created by jiangch on 2014/6/25.
 */
public class TitledWindow extends AnchorPane {

	private final Label titleLabel = new Label();
	private Pane parent;
	private final Label flagFreeButton = new Label();
	private final Label flagHoldButton = new Label();
	private Stage flapStage;
	private VBox contentPane = new VBox();

	public TitledWindow(Pane parent){

		setParent(parent);
		setPrefWidth(600);
		setPrefHeight(300);
		HBox hBox = new HBox();
		hBox.setPrefWidth(20);
		hBox.setFillHeight(true);
		hBox.setStyle("-fx-base: RGB(217,234,246); -fx-background-color: linear-gradient(to bottom right, -fx-base, white)");
		hBox.setSpacing(2);
		titleLabel.setAlignment(Pos.CENTER_RIGHT);
		hBox.getChildren().add(titleLabel);
		flagFreeButton.setGraphic(new ImageView("com/hk/framework/ui/titledwindow/flap_free.png"));
		flagFreeButton.setCursor(Cursor.HAND);
		flagFreeButton.setVisible(true);
		flagFreeButton.setManaged(true);
		flagHoldButton.setGraphic(new ImageView("com/hk/framework/ui/titledwindow/flap_hold.png"));
		flagHoldButton.setCursor(Cursor.HAND);
		flagHoldButton.setVisible(false);
		flagHoldButton.setManaged(false);

		flagFreeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				handleFlapFree(mouseEvent);
			}

		});

		flagHoldButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				handleFlapHold(mouseEvent);
			}


		});
		//button.on
		hBox.getChildren().addAll(flagFreeButton, flagHoldButton);
		getChildren().add(hBox);
		setRightAnchor(hBox,0.0);
		setLeftAnchor(hBox,0.0);
		setTopAnchor(hBox,0.0);
		getChildren().add(contentPane);
		setRightAnchor(contentPane,0.0);
		setLeftAnchor(contentPane, 0.0);
		setTopAnchor(contentPane, 20.0);
		setBottomAnchor(contentPane,0.0);

	}

	public void setContent(Node pane){
		contentPane.getChildren().clear();
		contentPane.getChildren().addAll(pane);
		VBox.setVgrow(pane, Priority.ALWAYS);
	}

	private void handleFlapFree(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() >= 2) {
			flagFreeButton.setVisible(false);
			flagFreeButton.setManaged(false);
			flagHoldButton.setVisible(true);
			flagHoldButton.setManaged(true);
			parent.getChildren().remove(this);
			flapStage = new Stage();
			flapStage.setScene(new Scene(this));
			flapStage.show();
		}
	}

	private void handleFlapHold(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() >= 2) {
			flagFreeButton.setVisible(true);
			flagFreeButton.setManaged(true);
			flagHoldButton.setVisible(false);
			flagHoldButton.setManaged(false);
			parent.getChildren().add(this);
			flapStage.close();
		}
	}

	private void setParent(Pane parent) {
		this.parent = parent;
	}

	public void setTitleName(String titleName) {
		titleLabel.setText(titleName);
	}

}
