
package com.hk.remote.amqp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: yangtao Date: 13-1-25 上午11:15
 */
public class SessionCache {

	public static final String USER_ID = "userId";
	public static final String SESSION_ID = "sessionId";
	public static final String ENVIRONMENT = "environment";

	private static Map<String, Serializable> sessionMap;
	private static HashMap<String, Boolean> permissions;

	static {
		sessionMap = new HashMap<String, Serializable>();
	}

	public static Serializable get(String key) {
		return sessionMap.get(key);
	}

	public static void setUserId(String userId) {
		// 更改清算日功能菜单可见性由此系统变量的值决定（采用eclipse commands的visibleWhen systemTest实现）
		System.setProperty("currentUser", userId);
		sessionMap.put(USER_ID, userId);
	}

	public static void setSessionId(String sessionId) {
		sessionMap.put(SESSION_ID, sessionId);
	}

	public static void setEnvironment(String environment) {
		sessionMap.put(ENVIRONMENT, environment);
	}

	public static Map<String, Boolean> getPermissions() {
		return permissions;
	}

	public static void setPermissions(HashMap<String, Boolean> permissions) {
		SessionCache.permissions = permissions;
	}

}
