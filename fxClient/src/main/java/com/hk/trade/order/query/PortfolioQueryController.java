package com.hk.trade.order.query;

import com.hk.framework.JavaFxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/15.
 */
public class PortfolioQueryController extends JavaFxController {

    @FXML
    public TableView portifolioTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<TableColumn> columns = portifolioTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory("portfolioCode"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory("portfolioName"));
        ObservableList<PortfolioQueryResult> datas = FXCollections.observableArrayList();
        portifolioTable.setItems(datas);
        PortfolioQueryResult testCase = new PortfolioQueryResult();
        testCase.setPortfolioCode("003003");
        testCase.setPortfolioName("超级组合");
        datas.add(testCase);
        PortfolioQueryResult testCase2 = new PortfolioQueryResult();
        testCase2.setPortfolioCode("003004");
        testCase2.setPortfolioName("神级组合");
        datas.add(testCase2);
    }
}
