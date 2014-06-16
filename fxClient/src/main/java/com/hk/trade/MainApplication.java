package com.hk.trade;

import com.hk.framework.JavaFxApplication;
import javafx.application.Application;

/**
 * Created by jiangch on 2014/6/7.
 * 主程序：默认启动登陆页面，登陆成功后跳转到主界面
 */
public class MainApplication extends JavaFxApplication {

    public static void main(String[] args) {
        Application.launch(MainApplication.class,args);
    }

    @Override
    protected Configuration buildGlobalConfiguration() {
        return new Configuration("投资交易","order/query/PortfolioQuery",800,600);
    }


}
