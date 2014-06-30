package com.hk.trade.order;

import com.hk.framework.FxmlLoadUtils;
import com.hk.marketdata.MarketDataPane;
import com.hk.framework.ui.tableview.TableViewFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jidefx.scene.control.field.NumberField;
import org.datafx.provider.ListDataProvider;
import org.datafx.reader.ArrayDataReader;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/26.
 */
public class StockOrderController implements Initializable{
	@FXML
	public TableView capitalAccountTableView;
	@FXML
	public Button actionButton;
	@FXML
	public VBox marketDataParentPane;
	@FXML
	public ComboBox priceModeCombo;
	@FXML
	public ComboBox targetTypeCombo;
	@FXML
	public GridPane detailParameterGroup;
	@FXML
	public ToggleButton sellTagButton;
	@FXML
	public ToggleGroup sideToggleGroup;
	@FXML
	public ToggleButton buyTagButton;
	@FXML
	public VBox capitalAccountVBox;

	private NumberField priceField = new NumberField();
	private NumberField quantityField = new NumberField();
	private NumberField amountField = new NumberField();


	public static Pane getInstance(){
		return FxmlLoadUtils.loadFxml("/com/hk/trade/order/stockordermain.fxml").getPane();
	}
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		format(amountField, 10,3);
		format(priceField, 6, 3);
		format(quantityField, 10, 0);
		detailParameterGroup.add(priceField,1,3);
		detailParameterGroup.add(quantityField,1,4);
		detailParameterGroup.add(amountField,1,5);

		initPriceMode();
		initTargetType();
		initMarketDataPane();
		initLeftSideButtons();
		initCapitalAccountView();
	}

	private void initPriceMode() {
		ObservableList<NameValue> nameValues = FXCollections.observableArrayList();
		nameValues.addAll(new NameValue("限价","limited"),new NameValue("不限价","unlimited"));
		priceModeCombo.setItems(nameValues);
		priceModeCombo.getSelectionModel().select(0);
	}


	private void initTargetType() {
		ObservableList<NameValue> nameValues = FXCollections.observableArrayList();
		nameValues.addAll(new NameValue("绝对数量","unlimited"),new NameValue("绝对金额","limited"));
		targetTypeCombo.setItems(nameValues);
		targetTypeCombo.getSelectionModel().select(0);
	}


	private void initLeftSideButtons() {
		sideToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle2) {
				if (toggle2 == buyTagButton) {
					actionButton.getStyleClass().add("buy-execution-button");
					actionButton.getStyleClass().remove("sell-execution-button");
				} else if (toggle2 == sellTagButton) {
					actionButton.getStyleClass().add("sell-execution-button");
					actionButton.getStyleClass().remove("buy-execution-button");
				} else {
					actionButton.getStyleClass().remove("sell-execution-button");
					actionButton.getStyleClass().remove("buy-execution-button");
				}
			}
		});
		buyTagButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				buyTagButton.setSelected(true);
			}
		});
		sellTagButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				sellTagButton.setSelected(true);
			}
		});
	}

	private void initMarketDataPane() {
		MarketDataPane e = new MarketDataPane();
		marketDataParentPane.getChildren().add(e);
		e.start();
	}

	private void initCapitalAccountView() {
		ObservableList<CapitalAccount> capitalAccountList = FXCollections.observableArrayList();
		capitalAccountList.add(new CapitalAccount("001088","千万资金"));
		capitalAccountList.add(new CapitalAccount("001999","亿万资金"));
		CapitalAccount[] nexts = {new CapitalAccount("1","2")};
		ListDataProvider<CapitalAccount> capitalAccountListDataProvider = new ListDataProvider<>(new ArrayDataReader<>(nexts));
		capitalAccountListDataProvider.setResultObservableList(capitalAccountList);
		TableView tableView = TableViewFactory.createTableView(capitalAccountList, CapitalAccount.class);
		capitalAccountVBox.getChildren().add(tableView);
		VBox.setVgrow(tableView, Priority.ALWAYS);
		capitalAccountListDataProvider.retrieve();
	}

	private void format(NumberField numberField, int maximumIntegerDigits, int maxinumFactionDigits) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setMaximumFractionDigits(maxinumFactionDigits);
		decimalFormat.setMinimumFractionDigits(0);
		decimalFormat.setMaximumIntegerDigits(maximumIntegerDigits);
		numberField.setSpinnersVisible(false);
		numberField.setDecimalFormat(decimalFormat);
	}

}
