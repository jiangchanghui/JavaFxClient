package com.hk.framework.ui;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangch on 2014/6/24.
 */
public class SecurityDataTest {

	@Test
	public void test(){
		Assert.assertEquals("60000     �ַ�����", new SecurityData("60000","�ַ�����").toString());
		Assert.assertEquals("I1402      ������", new SecurityData("I1402","������").toString());
	}
}
