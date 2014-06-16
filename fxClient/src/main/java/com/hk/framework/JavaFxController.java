package com.hk.framework;

import javafx.fxml.Initializable;

/**
 * Created by jiangch on 2014/6/11.
 */
public abstract class JavaFxController implements Initializable {

    protected JavaFxApplication application;

    protected JavaFxController setUp(JavaFxApplication application) {
        this.application = application;
        return this;
    }
}
