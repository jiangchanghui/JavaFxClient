package com.hk.framework.ui;

import javafx.animation.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class, with the UndecoratorController, is the central class for the
 * decoration of Transparent Stages. The Stage Undecorator TODO: Themes, manage
 * Quit (main primaryStage)
 * <p>
 * Bugs (Mac only?): Accelerators + Fullscreen crashes JVM KeyCombination does
 * not respect keyboard's locale Multi screen: On second screen JFX returns
 * wrong value for MinY (300)
 */
public class Undecorator extends StackPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(Undecorator.class);
	private static ResourceBundle LOC;

	private static int RESIZE_PADDING = 5;
	private static int FEEDBACK_STROKE = 4;

	public static int SHADOW_WIDTH = 2;
	private static DropShadow dsFocused = new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, SHADOW_WIDTH, 0.1, 0, 0);
	private static DropShadow dsNotFocused = new DropShadow(BlurType.THREE_PASS_BOX, Color.DARKGREY, SHADOW_WIDTH, 0, 0, 0);

	public int SAVED_SHADOW_WIDTH = 2;
	private StageStyle stageStyle;
	private MenuItem maximizeMenuItem;
	private CheckMenuItem fullScreenMenuItem;
	private Region clientArea;
	private Pane stageDecoration = null;
	private Rectangle shadowRectangle;
	private Pane glassPane;
	private Rectangle dockFeedback;
	private FadeTransition dockFadeTransition;
	private Stage dockFeedbackPopup;
	private ParallelTransition parallelTransition;
	private UndecoratorController undecoratorController;
	private Stage stage;
	private Rectangle resizeRect;
	SimpleBooleanProperty maximizeProperty;
	private SimpleBooleanProperty minimizeProperty;
	private SimpleBooleanProperty closeProperty;
	private SimpleBooleanProperty fullscreenProperty;
	private String backgroundStyleClass = "undecorator-background";
	private TranslateTransition fullscreenButtonTransition;
	@FXML
	@SuppressWarnings("all")
	private Button menu;
	@FXML
	@SuppressWarnings("all")
	private Button close;
	@FXML
	@SuppressWarnings("all")
	private Button maximize;
	@FXML
	@SuppressWarnings("all")
	private Button minimize;
	@FXML
	@SuppressWarnings("all")
	private Button resize;
	@FXML
	@SuppressWarnings("all")
	private Button fullscreen;
	@FXML
	@SuppressWarnings("all")
	private Label title;

	public Undecorator(Stage stage, Region root) {
		this(stage, root, "stagedecoration.fxml", StageStyle.UNDECORATED);
	}

	public Undecorator(Stage stag, Region clientArea, String stageDecorationFxml, StageStyle st) {
		create(stag, clientArea, getClass().getResource(stageDecorationFxml), st);
	}

	public Undecorator(Stage stag, Region clientArea, URL stageDecorationFxmlAsURL, StageStyle st) {
		create(stag, clientArea, stageDecorationFxmlAsURL, st);
	}

	public SimpleBooleanProperty maximizeProperty() {
		return maximizeProperty;
	}

	public SimpleBooleanProperty minimizeProperty() {
		return minimizeProperty;
	}

	public SimpleBooleanProperty closeProperty() {
		return closeProperty;
	}

	public SimpleBooleanProperty fullscreenProperty() {
		return fullscreenProperty;
	}

	public void create(Stage stag, Region clientArea, URL stageDecorationFxmlAsURL, StageStyle st) {
		this.stage = stag;
		this.clientArea = clientArea;
		setStageStyle(st);
		loadConfig();

		// The controller
		undecoratorController = new UndecoratorController(this);
		undecoratorController.setAsStageDraggable(stage, clientArea);
		// UI part of the decoration
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(stageDecorationFxmlAsURL);
			fxmlLoader.setController(this);
			stageDecoration = (Pane) fxmlLoader.load();
		} catch (Exception ex) {
			LOGGER.error("Decorations not found", ex);
		}

		getStylesheets().add("/com/hk/framework/ui/skin/undecorator.css");
		setButtonsVisible();
		maximizeActionSetting();
		fullScreenActionSetting();
		minimizeActionSetting();
		closeActionSetting();
		title.setText(stage.getTitle());
		title.getStyleClass().add("undecorator-label-titlebar");

		// Focus drop shadows: radius, spread, offsets

		resizeRect = new Rectangle();
		resizeRect.setFill(null);
		resizeRect.setStrokeWidth(RESIZE_PADDING);
		resizeRect.setStrokeType(StrokeType.INSIDE);
		resizeRect.setStroke(Color.TRANSPARENT);
		undecoratorController.setStageResizableWith(stage, resizeRect, RESIZE_PADDING, SHADOW_WIDTH);

		// Glass Pane
		glassPane = new Pane();
		glassPane.setMouseTransparent(true);
		buildDockFeedbackStage();
		shadowRectangle = new Rectangle();
		shadowRectangle.getStyleClass().add(backgroundStyleClass);
		shadowRectangle.setMouseTransparent(true);
		super.getChildren().addAll(shadowRectangle, clientArea, stageDecoration, resizeRect, glassPane);

        /*
         * Focused primaryStage
         */
		stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				setShadowFocused(t1.booleanValue());
			}
		});

		fullScreentActionSetting();

		computeAllSizes();
	}

	private void setButtonsVisible() {
		// If not resizable (quick fix)
		if (fullscreen != null) {
			fullscreen.setVisible(stage.isResizable());
		}
		resize.setVisible(stage.isResizable());
		if (maximize != null) {
			maximize.setVisible(stage.isResizable());
		}
		if (minimize != null && !stage.isResizable()) {
			AnchorPane.setRightAnchor(minimize, 34d);
		}
	}

	private void fullScreentActionSetting() {
		if (fullscreen == null) {
			return;
		}

		fullscreen.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (stage.isFullScreen()) {
					fullscreen.setOpacity(1);
				}
			}
		});
		fullscreen.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (stage.isFullScreen()) {
					fullscreen.setOpacity(0.4);
				}
			}
		});

		stage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean fullscreenState) {
				boolean fullScreenStateValue = fullscreenState.booleanValue();
				setShadow(!fullScreenStateValue);
				maximize.setVisible(!fullScreenStateValue);
				minimize.setVisible(!fullScreenStateValue);
				resize.setVisible(!fullScreenStateValue);
				if (fullScreenStateValue) {
					// 现在是全屏
					System.out.println("a");
					fullscreen.getStyleClass().add("decoration-button-unfullscreen");
					fullscreen.setTooltip(new Tooltip(LOC.getString("Restore")));
					undecoratorController.saveFullScreenBounds();
					fullscreen.setOpacity(0.2);
					fullScreenButtonTransition(66);
				} else {
					System.out.println("b");
					// 现在不是全屏
					//TODO:jiangch 这里为什么是这个style class
					fullscreen.getStyleClass().remove("decoration-button-unfullscreen");
					fullscreen.setTooltip(new Tooltip(LOC.getString("FullScreen")));
					undecoratorController.restoreFullScreenSavedBounds(stage);
					fullscreen.setOpacity(1);
					fullScreenButtonTransition(0);
				}
			}
		});

	}

	private void fullScreenButtonTransition(int x) {
		if (fullscreenButtonTransition != null) {
			fullscreenButtonTransition.stop();
		}
		fullscreenButtonTransition = new TranslateTransition();
		fullscreenButtonTransition.setDuration(Duration.millis(1000));
		fullscreenButtonTransition.setToX(x);
		fullscreenButtonTransition.setNode(fullscreen);
		fullscreenButtonTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				fullscreenButtonTransition = null;
			}
		});
		fullscreenButtonTransition.play();
	}

	/**
	 * Install default accelerators
	 *
	 * @param scene
	 */
	public void installAccelerators(Scene scene) {
		// Accelerators
		if (stage.isResizable()) {
			scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN, KeyCombination.SHORTCUT_DOWN), new Runnable() {
				@Override
				public void run() {
					switchFullscreen();
				}
			});
		}
		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN), new Runnable() {
			@Override
			public void run() {
				getController().minimize();
			}
		});
		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN), new Runnable() {
			@Override
			public void run() {
				getController().close();
			}
		});
	}

	/**
	 * Init the minimum/pref/max size in order to be reflected in the primary
	 * primaryStage
	 */
	private void computeAllSizes() {
		double minWidth = minWidth(getHeight());
		setMinWidth(minWidth);
		double minHeight = minHeight(getWidth());
		setMinHeight(minHeight);
		double prefHeight = prefHeight(getWidth());
		setPrefHeight(prefHeight);
		double prefWidth = prefWidth(getHeight());
		setPrefWidth(prefWidth);
		double maxWidth = maxWidth(getHeight());
		if (maxWidth > 0) {
			setMaxWidth(maxWidth);
		}
		double maxHeight = maxHeight(getWidth());
		if (maxHeight > 0) {
			setMaxHeight(maxHeight);
		}
	}
	/*
	 * The sizing is based on client area's bounds.
     */

	@Override
	protected double computePrefWidth(double d) {
		return clientArea.getPrefWidth() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
	}

	@Override
	protected double computePrefHeight(double d) {
		return clientArea.getPrefHeight() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
	}

	@Override
	protected double computeMaxHeight(double d) {
		return clientArea.getMaxHeight() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
	}

	@Override
	protected double computeMinHeight(double d) {
		double d2 = super.computeMinHeight(d);
		d2 += SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
		return d2;
	}

	@Override
	protected double computeMaxWidth(double d) {
		return clientArea.getMaxWidth() + SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
	}

	@Override
	protected double computeMinWidth(double d) {
		double d2 = super.computeMinWidth(d);
		d2 += SHADOW_WIDTH * 2 + RESIZE_PADDING * 2;
		return d2;
	}

	public StageStyle getStageStyle() {
		return stageStyle;
	}

	public void setStageStyle(StageStyle st) {
		stageStyle = st;
	}

	/**
	 * Activate fade in transition on showing event
	 */
	public void setFadeInTransition() {
		super.setOpacity(0);
		stage.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				if (t1.booleanValue()) {
					FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), Undecorator.this);
					fadeTransition.setToValue(1);
					fadeTransition.play();
				}
			}
		});
	}

	/**
	 * Launch the fade out transition. Must be invoked when the
	 * application/window is supposed to be closed
	 */
	public void setFadeOutTransition() {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), Undecorator.this);
		fadeTransition.setToValue(0);
		fadeTransition.play();
		fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				stage.hide();
				if (dockFeedbackPopup != null && dockFeedbackPopup.isShowing()) {
					dockFeedbackPopup.hide();
				}
			}
		});
	}

	public void removeDefaultBackgroundStyleClass() {
		shadowRectangle.getStyleClass().remove(backgroundStyleClass);
	}

	public Rectangle getBackgroundNode() {
		return shadowRectangle;
	}

	private void closeActionSetting() {
		// Close button
		close.setTooltip(new Tooltip(LOC.getString("Close")));
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				getController().close();
			}
		});
	}

	private void minimizeActionSetting() {
		// Minimize button
		if (minimize != null) { // Utility Stage
			minimize.setTooltip(new Tooltip(LOC.getString("Minimize")));
			minimize.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					getController().minimize();
				}
			});
		}
	}

	private void fullScreenActionSetting() {
		fullscreenProperty = new SimpleBooleanProperty(false);
		fullscreenProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				getController().setFullScreen(!stage.isFullScreen());
			}
		});
		if (fullscreen != null) { // Utility Stage
			fullscreen.setTooltip(new Tooltip(LOC.getString("FullScreen")));
			fullscreen.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					switchFullscreen();
				}
			});
		}
	}

	private void maximizeActionSetting() {
		maximizeProperty = new SimpleBooleanProperty(false);
		maximizeProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				getController().maximizeOrRestore();
			}
		});
		// Maximize button
		// If changed via contextual menu
		maximizeProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				Tooltip tooltip = maximize.getTooltip();
				if (tooltip.getText().equals(LOC.getString("Maximize"))) {
					tooltip.setText(LOC.getString("Restore"));
					maximize.getStyleClass().add("decoration-button-restore");
					resize.setVisible(false);
				} else {
					tooltip.setText(LOC.getString("Maximize"));
					maximize.getStyleClass().remove("decoration-button-restore");
					resize.setVisible(true);
				}
			}
		});
		if (maximize != null) { // Utility Stage
			maximize.setTooltip(new Tooltip(LOC.getString("Maximize")));
			maximize.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					switchMaximize();
				}
			});
		}
	}

	public void switchFullscreen() {
		// Invoke runLater even if it's on EDT: Crash apps on Mac
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				undecoratorController.setFullScreen(!stage.isFullScreen());
			}
		});
	}

	public void switchMaximize() {
		maximizeProperty().set(!maximizeProperty().get());
	}

	/**
	 * Bridge to the controller to enable the specified node to drag the primaryStage
	 *
	 * @param stage
	 * @param node
	 */
	public void setAsStageDraggable(Stage stage, Node node) {
		undecoratorController.setAsStageDraggable(stage, node);
	}

	/**
	 * Switch the visibility of the window's drop shadow
	 */
	protected void setShadow(boolean shadow) {
		// Already removed?
		if (!shadow && shadowRectangle.getEffect() == null) {
			return;
		}
		// From fullscreen to maximize case
		if (shadow && maximizeProperty.get()) {
			return;
		}
		if (!shadow) {
			shadowRectangle.setEffect(null);
			SAVED_SHADOW_WIDTH = SHADOW_WIDTH;
			SHADOW_WIDTH = 0;
		} else {
			shadowRectangle.setEffect(dsFocused);
			SHADOW_WIDTH = SAVED_SHADOW_WIDTH;
		}
	}

	/**
	 * Set on/off the primaryStage shadow effect
	 *
	 * @param b
	 */
	protected void setShadowFocused(boolean b) {
		// Do not change anything while maximized (in case of dialog closing for instance)
		if (stage.isFullScreen())
			return;
		if (maximizeProperty().get())
			return;
		if (b) {
			shadowRectangle.setEffect(dsFocused);
		} else {
			shadowRectangle.setEffect(dsNotFocused);
		}
	}

	/**
	 * Set the layout of different layers of the primaryStage
	 */
	@Override
	public void layoutChildren() {
		Bounds b = super.getLayoutBounds();
		double w = b.getWidth();
		double h = b.getHeight();
		ObservableList<Node> list = super.getChildren();
		for (Node node : list) {
			if (node == shadowRectangle) {
				shadowRectangle.setWidth(w - SHADOW_WIDTH * 2);
				shadowRectangle.setHeight(h - SHADOW_WIDTH * 2);
				shadowRectangle.setX(SHADOW_WIDTH);
				shadowRectangle.setY(SHADOW_WIDTH);
			} else if (node == stageDecoration) {
				stageDecoration.resize(w - SHADOW_WIDTH * 2, h - SHADOW_WIDTH * 2);
				stageDecoration.setLayoutX(SHADOW_WIDTH);
				stageDecoration.setLayoutY(SHADOW_WIDTH);
			} else if (node == resizeRect) {
				resizeRect.setWidth(w - SHADOW_WIDTH * 2);
				resizeRect.setHeight(h - SHADOW_WIDTH * 2);
				resizeRect.setLayoutX(SHADOW_WIDTH);
				resizeRect.setLayoutY(SHADOW_WIDTH);
			} else {
				node.resize(w - SHADOW_WIDTH * 2 - RESIZE_PADDING * 2, h - SHADOW_WIDTH * 2 - RESIZE_PADDING * 2);
				node.setLayoutX(SHADOW_WIDTH + RESIZE_PADDING);
				node.setLayoutY(SHADOW_WIDTH + RESIZE_PADDING);
			}
		}
	}

	public UndecoratorController getController() {
		return undecoratorController;
	}

	public Stage getStage() {
		return stage;
	}

	protected Pane getGlassPane() {
		return glassPane;
	}

	public void addGlassPane(Node node) {
		glassPane.getChildren().add(node);
	}

	public void removeGlassPane(Node node) {
		glassPane.getChildren().remove(node);
	}

	/**
	 * Returns the decoration (buttons...)
	 *
	 * @return
	 */
	public Pane getStageDecorationNode() {
		return stageDecoration;
	}

	/**
	 * Prepare Stage for dock feedback display
	 */
	void buildDockFeedbackStage() {
		dockFeedbackPopup = new Stage(StageStyle.TRANSPARENT);
		dockFeedback = new Rectangle(0, 0, 100, 100);
		dockFeedback.setArcHeight(10);
		dockFeedback.setArcWidth(10);
		dockFeedback.setFill(Color.TRANSPARENT);
		dockFeedback.setStroke(Color.BLACK);
		dockFeedback.setStrokeWidth(2);
		dockFeedback.setCache(true);
		dockFeedback.setCacheHint(CacheHint.SPEED);
		dockFeedback.setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.BLACK, 10, 0.2, 3, 3));
		dockFeedback.setMouseTransparent(true);
		BorderPane borderpane = new BorderPane();
		borderpane.setCenter(dockFeedback);
		Scene scene = new Scene(borderpane);
		scene.setFill(Color.TRANSPARENT);
		dockFeedbackPopup.setScene(scene);
		dockFeedbackPopup.sizeToScene();
	}
	/* void buildDockFeedback() {
	 `   dockFeedbackPopup = new Popup();
     dockFeedbackPopup.setHideOnEscape(false);
     dockFeedbackPopup.setAutoFix(false);
     dockFeedback = new Rectangle(0, 0, 100, 100);
     dockFeedback.setFill(Color.TRANSPARENT);
     dockFeedback.setStroke(Color.BLACK);
     dockFeedback.setStrokeWidth(2);
     dockFeedback.setMouseTransparent(true);
      
     // dockFeedback.setStyle("-fx-border-color:black; -fx-border-width:1"); //-fx-background-color: #FFFFFFFF; -fx-background-insets:10;");
     dockFeedback.setEffect(new DropShadow(SHADOW_WIDTH, Color.BLACK));
     //BorderPane borderpane = new BorderPane();
     //        borderpane.setCenter(dockFeedback);
     dockFeedbackPopup.getContent().add(dockFeedback);
     //        dockFeedbackPopup.sizeToScene();

     }*/

	/**
	 * Activate dock feedback on screen's bounds
	 *
	 * @param x
	 * @param y
	 */
	public void setDockFeedbackVisible(double x, double y, double width, double height) {
		dockFeedbackPopup.setX(x);
		dockFeedbackPopup.setY(y);
		dockFeedback.setX(SHADOW_WIDTH);
		dockFeedback.setY(SHADOW_WIDTH);
		dockFeedback.setHeight(height - SHADOW_WIDTH * 2);
		dockFeedback.setWidth(width - SHADOW_WIDTH * 2);
		dockFeedbackPopup.setWidth(width);
		dockFeedbackPopup.setHeight(height);
		dockFeedback.setOpacity(1);
		dockFeedbackPopup.show();
		dockFadeTransition = FadeTransitionBuilder.create()
				                     .duration(Duration.millis(200))
				                     .node(dockFeedback)
				                     .fromValue(0)
				                     .toValue(1)
				                     .autoReverse(true)
				                     .cycleCount(3)
				                     .onFinished(new EventHandler<ActionEvent>() {
					                     @Override
					                     public void handle(ActionEvent t) {
						                     //dockFeedback.setVisible(false);
						                     //dockFeedbackPopup.hide();
					                     }
				                     })
				                     .build();
        /*
         ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
         .duration(Duration.millis(1000))
         .node(dockFeedback)
         .fromX(0.4)
         .fromY(0.4)
         .toX(1)
         .toY(1)
         .build();

         TranslateTransition translateTransition = TranslateTransitionBuilder.create()
         .duration(Duration.millis(2000))
         .node(dockFeedback)
         .fromY((height * 0.4) / 2)
         .toY(0)
         .build();

         parallelTransition = new ParallelTransition(dockFeedback);
         parallelTransition.getChildren().addAll(dockFadeTransition,scaleTransition);
         parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent t) {
         //dockFeedback.setVisible(false);
         dockFeedbackPopup.hide();
         }
         });
         parallelTransition.play();*/
		dockFadeTransition.play();
	}

	public void setDockFeedbackInvisible() {
		if (dockFeedbackPopup.isShowing()) {
			dockFeedbackPopup.hide();
			if (dockFadeTransition != null) {
				dockFadeTransition.stop();
			}
		}
	}

	void loadConfig() {
		LOC = ResourceBundle.getBundle("localization", Locale.getDefault());
	}
}
