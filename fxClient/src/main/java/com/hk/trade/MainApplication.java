package com.hk.trade;

import com.hk.framework.JavaFxApplication;
import javafx.application.Application;

/**
 * Created by jiangch on 2014/6/7.
 * ������Ĭ��������½ҳ�棬��½�ɹ�����ת��������
 */
public class MainApplication extends JavaFxApplication {

    public static void main(String[] args) {
        Application.launch(MainApplication.class,args);
    }

    @Override
    protected Configuration buildGlobalConfiguration() {
        return new Configuration("Ͷ�ʽ���","order/query/PortfolioQuery",800,600);
    }


}
