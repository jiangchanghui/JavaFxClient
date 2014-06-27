package com.hk.framework.ui;

import com.hk.marketdata.SecurityData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangch on 2014/6/24.
 */
public class SecurityDataTest {

	@Test
	public void test(){
		Assert.assertEquals("60000     浦发银行", new SecurityData("60000","浦发银行").toString());
		Assert.assertEquals("I1402      当月银", new SecurityData("I1402","当月银").toString());
	}
}
