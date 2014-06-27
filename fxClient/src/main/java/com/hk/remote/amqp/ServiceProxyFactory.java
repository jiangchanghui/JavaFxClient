
package com.hk.remote.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class ServiceProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> clazz, int timeout) {
		RabbitTemplate rabbitTemplate = RabbitConfiguration.rabbitTemplate();
		if (rabbitTemplate == null) {
			throw new RuntimeException("消息服务器已经宕机，请联系管理员。");
		}
		AmqpProxyFactoryBean amqpProxyFactoryBean = new AmqpProxyFactoryBean();
		amqpProxyFactoryBean.setAmqpTemplate(rabbitTemplate);
		amqpProxyFactoryBean.setServiceInterface(clazz);
		amqpProxyFactoryBean.afterPropertiesSet();
		amqpProxyFactoryBean.setRemoteInvocationFactory(new SimpleRemoteInvocationFactory());

		Object springOrigProxy = createProxy(amqpProxyFactoryBean);
		RabbitMethodInterceptor methodInterceptor = new RabbitMethodInterceptor(timeout);

		ProxyFactory factory = new ProxyFactory();
		factory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, methodInterceptor));
		factory.setProxyTargetClass(false);
		factory.addInterface(clazz);
		factory.setTarget(springOrigProxy);
		return (T) factory.getProxy();

	}

	public static <T> T createProxy(Class<T> clazz) {
		return createProxy(clazz, 30);
	}

	/**
	 * @param <T>
	 * @param amqpProxyFactoryBean
	 * @return
	 */
	private static Object createProxy(AmqpProxyFactoryBean amqpProxyFactoryBean) {
		try {
			return amqpProxyFactoryBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
