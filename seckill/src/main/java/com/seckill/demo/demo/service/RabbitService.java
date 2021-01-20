package com.seckill.demo.demo.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 发送到mq 业务
 *<p>Description: </p>
 * @ClassName: RabbitService
 * @author wangwenzhao
 * @date Jan 20, 20213:28:20 PM
 * @version: V1.0
 */
@Component
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String queue, String message) {
        System.out.println("send message: " + message + " to routing: " + queue);
        rabbitTemplate.convertAndSend(queue, message);
    }

    /**
     * 发送消息至延迟交换机，并且绑定延迟路由
     */
    public void sendDelay(Object object, Long expire) {
        Message message;
        try {
            message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(object))
                    .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).setExpiration(String.valueOf(expire)).build();
            this.rabbitTemplate.convertAndSend("order_delay_exchange", "order_delay_routing_key", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
