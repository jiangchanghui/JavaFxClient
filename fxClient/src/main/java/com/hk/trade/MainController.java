package com.hk.trade;

import com.hk.framework.JavaFxController;
import com.panemu.tiwulfx.control.DetachableTab;
import com.panemu.tiwulfx.control.DetachableTabPane;
import com.panemu.tiwulfx.control.sidemenu.SideMenu;
import com.panemu.tiwulfx.control.sidemenu.SideMenuItem;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/8.
 */
public class MainController extends JavaFxController {

    @FXML
    public DetachableTabPane contentTab;
    @FXML
    public DetachableTabPane queryTab;
    @FXML
    public TreeView<String> mainMenu;
    @FXML
    public HBox mainHBox;
    @FXML
    public Label navSwitchLabel;
    @FXML
    public AnchorPane navigationPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText("���׹���");
        titledPane.setAnimated(false);
        TreeItem<String> rootItem = new TreeItem<String>("root");
        TreeItem<String> investMenu = new TreeItem<String>("Ͷ�ʹ���");
        TreeItem<String> stockOrderMenu = new TreeItem<String>("��Ʊָ��");
        investMenu.getChildren().add(stockOrderMenu);
        investMenu.getChildren().add(new TreeItem<String>("ծȯָ��"));
        investMenu.getChildren().add(new TreeItem<String>("�ڻ�ָ��"));
        rootItem.getChildren().add(investMenu);
        TreeItem<String> tradeMenu = new TreeItem<String>("���׹���");
        tradeMenu.getChildren().add(new TreeItem<String>("ͨ��ί��"));
        tradeMenu.getChildren().add(new TreeItem<String>("ί�в�ѯ"));
        rootItem.getChildren().add(tradeMenu);
        mainMenu.setRoot(rootItem);
        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(mouseEvent.getClickCount() == 2)
                {
                    TreeItem<String> item =mainMenu.getSelectionModel().getSelectedItem();
                    DetachableTab tab = new DetachableTab();
                    Label tabALabel = new Label(item.getValue());
                    tab.setGraphic(tabALabel);
                    contentTab.getTabs().add(tab);
                    contentTab.getSelectionModel().select(tab);
                    DetachableTab tab2 = new DetachableTab();
                    Label tabALabel2 = new Label(item.getValue()+"222");
                    tab2.setGraphic(tabALabel2);

                    queryTab.getTabs().add(tab2);
                    queryTab.getSelectionModel().select(tab2);

                }
            }
        });

        navSwitchLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2)
                {
                    if(mainHBox.getChildren().contains(navigationPane)) {
                        mainHBox.getChildren().remove(navigationPane);
                        navSwitchLabel.setText("չ������");
                    }else{
                        mainHBox.getChildren().add(0,navigationPane);
                        navSwitchLabel.setText("�رյ���");
                    }
                }
            }
        });
    }


}
