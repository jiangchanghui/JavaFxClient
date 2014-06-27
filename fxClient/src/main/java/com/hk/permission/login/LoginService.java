package com.hk.permission.login;

import com.hk.proxy.LoginCommandController;
import com.hk.proxy.amqp.SessionCache;
import elf.api.security.command.login.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by jiangch on 2014/6/23.
 */
public class LoginService extends Service<ObservableList<LoginResult>> {

	private AccountInfo accountInfo;

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	@Override
	protected Task<ObservableList<LoginResult>> createTask() {
		return new LoginTasks();
	}

	class LoginTasks extends Task<ObservableList<LoginResult>> {

		@Override
		protected ObservableList<LoginResult> call() throws Exception {
			//TODO:jiangch 完成真正的数据加载与网络初始化
			//初始化网络连接
			updateMessage("初始化网络");
			updateProgress(1, 4);
			Thread.sleep(200);
			updateMessage("验证用户名与密码");
			login();

			//验证用户名与密码
			updateProgress(2, 4);
			Thread.sleep(200);
			//加载证券数据
			updateProgress(3, 4);
			updateMessage("加载证券数据");
			Thread.sleep(200);
			//加载账户数据
			updateProgress(4, 4);
			updateMessage("加载账户数据");
			Thread.sleep(200);
			ObservableList<LoginResult> results = FXCollections.observableArrayList();
			LoginResult loginResult = new LoginResult();
			loginResult.setAccountNo(accountInfo.getAccountNo());
			results.add(loginResult);
			return results;
		}

		private void login() {
			if (mockUser(accountInfo.getAccountNo())) {
				return;
			}
			authenticate(accountInfo.getAccountNo(), accountInfo.getPassword());
		}

		private void authenticate(String account, String password) {
			Ticket tick = LoginCommandController.login(account, password);
			SessionCache.setUserId(account);
			String sessionId = tick.getSessionId();
			SessionCache.setSessionId(sessionId);
			SessionCache.setEnvironment(tick.getEnvironment());
			SessionCache.setPermissions(tick.getPermissions());
			SessionManger.setCurrentAccount(account);
		}

		private boolean mockUser(String account) {
			return "jiangch".equalsIgnoreCase(account);
		}
	}
}

