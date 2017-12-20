package com.bj58.timer.web.common;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.bj58.timer.web.constant.ServiceConstant;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class MqPublisher {
    
	private static volatile Channel channel;
	
	public static void publish(String msg,ConnectionFactory connectionFactory,String route) throws IOException, TimeoutException{
		if(channel == null){
			synchronized (MqPublisher.class) {
				if(channel == null){
					Connection connection = connectionFactory.newConnection();
					channel = connection.createChannel();
					channel.exchangeDeclare(ServiceConstant.TIMER_EXCHANGE, BuiltinExchangeType.DIRECT,true);
					//channel.queueDeclare(ServiceConstant.QUEUE_JOB_START,durable,false,false,null);
					//channel.queueDeclare(ServiceConstant.QUEUE_JOB_END,durable,false,false,null);
				}
			}
		}

		channel.basicPublish(ServiceConstant.TIMER_EXCHANGE, route, null, msg.getBytes("UTF-8"));  //如果队列不存在不会抛出异常,而且没有返回值
	}
}
