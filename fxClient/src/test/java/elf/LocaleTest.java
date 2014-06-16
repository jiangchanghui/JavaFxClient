package elf;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by jiangch on 2014/6/9.
 */
public class LocaleTest {

    @Test
    public void test(){
       Locale locale = Locale.SIMPLIFIED_CHINESE;
       ResourceBundle bundle = ResourceBundle.getBundle("controlsfx",locale);
        String value = bundle.getString("dlg.cancel.button");
        Assert.assertEquals("È¡Ïû",value);
    }
}
