package com.hk.permission.login;

/**
 * Created by jiangch on 2014/6/23.
 */
public class SessionManger {
	private static String currentAccount;

	public static String getCurrentAccount() {
		return currentAccount;
	}

	public static void setCurrentAccount(String currentAccount) {
		SessionManger.currentAccount = currentAccount;
	}
}
