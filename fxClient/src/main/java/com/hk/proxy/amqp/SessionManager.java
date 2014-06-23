
package com.hk.proxy.amqp;

import java.io.Serializable;
import java.util.Map;

/**
 * User: yangtao Date: 13-1-25 ÉÏÎç10:42
 */
public class SessionManager {

	public static String getUserId() {
		return (String) SessionCache.get(SessionCache.USER_ID);
	}

	public static Serializable getSessionId() {
		return SessionCache.get(SessionCache.SESSION_ID);
	}

	public static Serializable getEnvironment() {
		return SessionCache.get(SessionCache.ENVIRONMENT);
	}

	public static boolean isPermitted(String permisstionCode) {
		if (permisstionCode == null) {
			return true;
		}
		Map<String, Boolean> permissions = getPermissions();
		if (permissions == null) {
			return false;
		}
		Boolean allow = permissions.get(permisstionCode);
		if (allow == null) {
			return false;
		}
		return allow;
	}

	public static Map<String, Boolean> getPermissions() {
		return SessionCache.getPermissions();
	}
}
