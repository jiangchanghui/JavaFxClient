
package com.hk.remote.amqp;

import java.util.Properties;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitConfiguration {

	private static String CLIENT_SYNC_RCP_Q = "client.sync.queue";
	private static RabbitTemplate rabbitTemplate = null;
	private static RabbitAdmin rabbitAdmin = null;
	private static CachingConnectionFactory connectionFactory = null;

	/**
	 * 
	 */
	private static void init() {
		connectionFactory = newConnectionFactory();
		rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(new ElfMessageConverter());
		rabbitTemplate.setRoutingKey(CLIENT_SYNC_RCP_Q);
		rabbitTemplate.setReplyTimeout(-1);
		rabbitAdmin = new RabbitAdmin(connectionFactory);

	}

	private static CachingConnectionFactory newConnectionFactory() {
		Properties properties = getRabbitProperties();
		String host = "10.67.2.28";
		if (host == null) {
			host = properties.getProperty("host");
		}
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setRequestedHeartBeat(120);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}

	public static RabbitTemplate rabbitTemplate() {
		synchronized (RabbitConfiguration.class) {
			if (rabbitTemplate == null) {
				init();
			}
			return rabbitTemplate;
		}
	}

	public static RabbitAdmin getRabbitAdmin() {
		synchronized (RabbitConfiguration.class) {
			if (rabbitAdmin == null) {
				init();
			}
			return rabbitAdmin;
		}
	}

	public static CachingConnectionFactory getConnectionFactory() {
		synchronized (RabbitConfiguration.class) {
			if (connectionFactory == null) {
				init();
			}
			return connectionFactory;
		}
	}

	public static void destroy(){
		if(connectionFactory!=null) {
			connectionFactory.destroy();
		}
	}

	/**
	 * @return
	 */
	private static Properties getRabbitProperties() {
		try {
			Properties properties = new Properties();
			properties.load(RabbitConfiguration.class.getClassLoader().getResourceAsStream("rabbit-mq-config.properties"));
			return properties;
		} catch (Exception e) {
			return new Properties();
		}
	}
}
