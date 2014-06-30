package com.hk.login;

/**
 * Created by jiangch on 2014/6/23.
 */
public class AccountInfo {
	private final String accountNo;
	private final String password;

	public AccountInfo(String accountNo,String password) {
		this.password = password;
		this.accountNo = accountNo;
	}

	public String getAccountNo() {
		return accountNo;
	}


	public String getPassword() {
		return password;
	}
}
