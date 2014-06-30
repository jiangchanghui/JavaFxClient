
package com.hk.remote.amqp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.remoting.support.RemoteInvocationResult;

public class RabbitMethodInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMethodInterceptor.class);
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(20);
	private final int timeout;

	public static void destroy(){
		threadPool.shutdownNow();
	}

	public RabbitMethodInterceptor(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final LinkedBlockingQueue<RemoteInvocationResult> queue = new LinkedBlockingQueue<RemoteInvocationResult>(1);
		threadPool.execute(new InvocationExecutor(invocation, queue));
		RemoteInvocationResult remoteInvocationResult = queue.poll(timeout, TimeUnit.SECONDS);
		if (remoteInvocationResult != null) {
			return remoteInvocationResult.recreate();
		}
		LOGGER.error("获取结果超时：" + invocation.toString());
		return getServerTimeoutResult().recreate();
	}

	private RemoteInvocationResult getServerTimeoutResult() {
		return new RemoteInvocationResult(new RuntimeException("获取操作结果超时，请重新查询"));
	}

	private final class InvocationExecutor implements Runnable {

		private final MethodInvocation invocation;
		private final LinkedBlockingQueue<RemoteInvocationResult> queue;

		private InvocationExecutor(MethodInvocation invocation, LinkedBlockingQueue<RemoteInvocationResult> queue) {
			this.invocation = invocation;
			this.queue = queue;
		}

		@Override
		public void run() {
			RemoteInvocationResult result = execute(invocation);
			try {
				queue.put(result);
			} catch (InterruptedException e) {
				LOGGER.error("处理远程调用返回时发生错误", e);
			}
		}

		private RemoteInvocationResult execute(MethodInvocation invocation) {
			long startTime = System.currentTimeMillis();
			try {
				Object proceed = invocation.proceed();
				return new RemoteInvocationResult(proceed);
			} catch (AmqpConnectException ace) {
				LOGGER.error("调用方法" + invocation.getMethod() + ",参数(" + invocation.getArguments() + "+)发生异常：" + ace.getMessage(), ace);
				return new RemoteInvocationResult(new RuntimeException("网络异常无法连接服务器", ace));
			} catch (AmqpIOException ace) {
				LOGGER.error("调用方法" + invocation.getMethod() + ",参数(" + invocation.getArguments() + "+)发生异常：" + ace.getMessage(), ace);
				return new RemoteInvocationResult(new RuntimeException("网络异常无法连接服务器", ace));
			} catch (Throwable t) {
				LOGGER.error("调用方法" + invocation.getMethod() + ",参数(" + invocation.getArguments() + "+)发生异常：" + t.getMessage(), t);
				return new RemoteInvocationResult(t);
			} finally {
				long durTime = System.currentTimeMillis() - startTime;
				LOGGER.debug("调用方法" + invocation.getMethod() + ",参数(" + invocation.getArguments() + "+),运行时间(" + durTime + ")");
			}
		}

	}
}
