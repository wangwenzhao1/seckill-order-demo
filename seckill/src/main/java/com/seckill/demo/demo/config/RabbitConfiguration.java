package com.seckill.demo.demo.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import com.seckill.demo.demo.listener.RabbitMessageListener;
import com.seckill.demo.demo.util.RabbitConstants;


/**
 * @ClassName: RabbitConfiguration
 * @version: V1.0
 */
@Configuration
public class RabbitConfiguration implements RabbitListenerConfigurer {

    private final RabbitMessageListener rabbitMessageListener;

    @Autowired
    public RabbitConfiguration(RabbitMessageListener rabbitMessageListener) {
        this.rabbitMessageListener = rabbitMessageListener;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

    /**
     * 订单消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange assembleDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(RabbitConstants.EXCHANGE_NAME).durable().build();
    }

    /**
     * 订单实际消费队列
     */
    @Bean
    public Queue assembleQueue() {
        return new Queue(RabbitConstants.QUEUE_NAME);
    }

    /**
     * 将订单队列绑定到交换机
     */
    @Bean
    Binding assembleBinding(DirectExchange assembleDirect, Queue assembleQueue) {
        return BindingBuilder.bind(assembleQueue).to(assembleDirect).with(RabbitConstants.ROUTE_KEY);
    }


    /**
     * 订单延迟队列队列所绑定的交换机
     */
    @Bean
    DirectExchange assembleTtlDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(RabbitConstants.DELAY_EXCHANGE_NAME).durable().build();
    }

    /**
     * 订单延迟队列，设置了死信交换机和死信路由，从而时间到期之后进入死信队列
     * (消息->延迟队列->死信队列。发送条件:时间过期)
     */
    @Bean
    public Queue assembleTtlQueue() {
        return QueueBuilder
                .durable(RabbitConstants.DELAY_QUEUE_NAME)
                //到期后转发的交换机(死信交换机)
                .withArgument("x-dead-letter-exchange", RabbitConstants.EXCHANGE_NAME)
                //到期后转发的路由键(死信路由)
                .withArgument("x-dead-letter-routing-key", RabbitConstants.ROUTE_KEY)
                //队列过期时间
                .withArgument("x-message-ttl", 5 * 60 * 1000L)
                .build();
    }

    /**
     * 将订单延迟队列绑定到交换机
     */
    @Bean
    Binding assembleTtlBinding(DirectExchange assembleTtlDirect, Queue assembleTtlQueue) {
        return BindingBuilder.bind(assembleTtlQueue).to(assembleTtlDirect).with(RabbitConstants.DEALY_ROUTE_KEY);
    }

}
