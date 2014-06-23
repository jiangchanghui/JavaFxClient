package com.hk.proxy.amqp;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;

class SimpleRemoteInvocationFactory implements RemoteInvocationFactory {

	@Override
	public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
		RemoteInvocation remoteInvocation = new RemoteInvocation(methodInvocation);
		remoteInvocation.addAttribute("serviceInterface", methodInvocation.getMethod().getDeclaringClass().getName());
		Serializable sessionId = SessionManager.getSessionId();
		remoteInvocation.addAttribute("sessionId", sessionId);
		Serializable userId = SessionManager.getUserId();
		remoteInvocation.addAttribute("userId", userId);
		return remoteInvocation;
	}
}