package com.hk.framework.ui.tableview;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangch on 2014/6/27.
 */
public class TableViewFactory {

	public static TableView createTableView(ObservableList capitalAccountList, Class<?> clazz) {
		TableView tableView = new TableView();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			TableColumnName[] tableColumnName = field.getAnnotationsByType(TableColumnName.class);
			if(tableColumnName == null||tableColumnName.length==0){
				continue;
			}
			String columnName = tableColumnName[0].value();
			TableColumn tableColumn = new TableColumn(columnName);
			tableColumn.setCellValueFactory(new PropertyValueFactory(field.getName()));
			tableView.getColumns().add(tableColumn);
		}
		tableView.setItems(capitalAccountList);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setTableMenuButtonVisible(true);
		return tableView;
	}

	public static List<TableColumn> createTableColumn(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		final List<TableColumn> tableColumns = new ArrayList<>();
		for (Field field : fields) {
			TableColumnName[] tableColumnName = field.getAnnotationsByType(TableColumnName.class);
			if(tableColumnName == null||tableColumnName.length==0){
				continue;
			}
			String columnName = tableColumnName[0].value();
			TableColumn tableColumn = new TableColumn(columnName);
			tableColumn.setCellValueFactory(new PropertyValueFactory(field.getName()));
			tableColumns.add(tableColumn);
		}
		return tableColumns;
	}
}
