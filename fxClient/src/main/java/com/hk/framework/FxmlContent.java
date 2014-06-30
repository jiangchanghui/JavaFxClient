package com.hk.framework;

import javafx.scene.layout.Pane;

/**
 * Created by jiangch on 2014/6/21.
 */
public class FxmlContent {

	private final Pane pane;
	private final Object controller;

	public FxmlContent(Pane pane, Object controller) {
		this.pane = pane;
		this.controller = controller;
	}

	public Pane getPane() {
		return pane;
	}

	public Object getController() {
		return controller;
	}

}
