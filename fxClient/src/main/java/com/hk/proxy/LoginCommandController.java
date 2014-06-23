package com.hk.proxy;

import java.util.Collections;
import java.util.List;

import com.hk.proxy.amqp.ServiceProxyFactory;
import elf.api.security.command.exception.SecurityException;
import elf.api.security.command.login.LoginCommand;
import elf.api.security.command.login.LoginEvent;
import elf.api.security.command.login.ModifyPasswordEvent;
import elf.api.security.command.login.Ticket;

public class LoginCommandController {

	private static LoginCommand proxy;

	public static Ticket login(String username, String password) {
		LoginEvent loginEvent = new LoginEvent();
		loginEvent.setUsername(username);
		loginEvent.setPassword(password);
		loginEvent.setPermissions(Collections.emptyList());
		loginEvent.setIp("localhost");
		loginEvent.setAddress("localhost");
		loginEvent.setMac("localhost");
		return getService().login(loginEvent);
	}

	public static void logout(String sessionId) {
		getService().logout(sessionId);
	}

	public static void modifyPassword(String userCode, String password, String newPassword) throws SecurityException {
		ModifyPasswordEvent modifyPasswordEvent = new ModifyPasswordEvent();
		modifyPasswordEvent.setUserCode(userCode);
		modifyPasswordEvent.setPassword(password);
		modifyPasswordEvent.setNewPassword(newPassword);
		getService().modifyPassword(modifyPasswordEvent);
	}

	private static synchronized LoginCommand getService() {
		if (proxy == null) {
			proxy = ServiceProxyFactory.createProxy(LoginCommand.class);
		}
		return proxy;
	}
}
