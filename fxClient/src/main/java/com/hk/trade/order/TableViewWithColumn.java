package com.hk.trade.order;

import com.hk.trade.ui.TableViewFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Created by jiangch on 2014/6/27.
 */
public class TableViewWithColumn extends TableView {

	public TableViewWithColumn(Class<?> clazz) {
		super();
		List<TableColumn> tableColumns = TableViewFactory.createTableColumn(clazz);
		getColumns().addAll(tableColumns);
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

}
