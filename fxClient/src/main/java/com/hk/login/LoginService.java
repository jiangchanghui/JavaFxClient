package com.hk.login;

import com.hk.remote.LoginCommandController;
import com.hk.remote.amqp.SessionCache;
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
			//TODO:jiangch ������������ݼ����������ʼ��
			//��ʼ����������
			updateMessage("��ʼ������");
			updateProgress(1, 4);
			Thread.sleep(200);
			updateMessage("��֤�û���������");
			login();

			//��֤�û���������
			updateProgress(2, 4);
			Thread.sleep(200);
			//����֤ȯ����
			updateProgress(3, 4);
			updateMessage("����֤ȯ����");
			Thread.sleep(200);
			//�����˻�����
			updateProgress(4, 4);
			updateMessage("�����˻�����");
			Thread.sleep(200);
			ObservableList<LoginResult> results = FXCollections.observableArrayList();
			LoginResult loginResult = new LoginResult();
			loginResult.setAccountNo(accountInfo.getAccountNo());
			results.add(loginResult);
			return results;
		}

		private void login() {
			if (mockUser(accountInfo.getAccountNo())) {
				throw new RuntimeException("��ֹ��½");
				//return;
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

