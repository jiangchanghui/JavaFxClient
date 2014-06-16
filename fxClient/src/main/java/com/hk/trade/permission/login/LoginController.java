package com.hk.trade.permission.login;

import com.hk.framework.JavaFxController;
import javafx.fxml.Initializable;

import com.hk.trade.permission.model.AccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jiangch on 2014/6/7.
 */
public class LoginController extends JavaFxController {

    @FXML
    private CustomTextField accountNo;
    @FXML
    private CustomPasswordField accountPassword;
    @FXML
    private Label loginErrorMessage;

    public void login(ActionEvent actionEvent) {
        String account = accountNo.getText();
        String password = accountPassword.getText();
        if(AccountService.valid(account,password)){
            application.switchScene("main");
            return;
        }
        loginErrorMessage.setText("账号或者密码不正确");
    }

    public void logout(ActionEvent actionEvent) {
        loginErrorMessage.setText("logout press");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginErrorMessage.setText("");
    }
}
