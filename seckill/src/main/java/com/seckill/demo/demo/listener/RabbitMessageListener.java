package com.seckill.demo.demo.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.seckill.demo.demo.entry.OrderVO;
import com.seckill.demo.demo.entry.ZOrder;
import com.seckill.demo.demo.mapper.ZOrderMapper;
import com.seckill.demo.demo.service.RabbitmqService;
import com.seckill.demo.demo.util.RabbitConstants;
import com.seckill.demo.demo.util.TimeUtils;

/**
 * 监听 消费消息
 * @ClassName: RabbitMessageListener
 * @author wangwenzhao
 * @date Jan 20, 20213:30:59 PM
 * @version: V1.0
 */
@Component
public class RabbitMessageListener {

    private Logger logger  = LoggerFactory.getLogger(RabbitMessageListener.class);


    private static RabbitmqService rabbitmqService;

    @Autowired
    public RabbitMessageListener(RabbitmqService rabbitmqService) {
        this.rabbitmqService = rabbitmqService;

    }


    @Autowired
    private ZOrderMapper zOrderMapper;

    /**
     * 订单延迟消费
     * @author wangwenzhao
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitConstants.QUEUE_NAME})
    @RabbitHandler
    public void orderDelayQueue(Message message, Channel channel) {
        try {
            String strObject = new String(message.getBody(), "UTF-8");
            OrderVO delayOrder = JSONObject.parseObject(strObject, OrderVO.class);
            logger.info("死信队列消息信息：{} ", delayOrder);
            try {
                //手动ack
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                /**
                 *  监听到过期订单之后，先查询改订单信息，要是还没有支付，则取消改订单
                 */
                ZOrder order = zOrderMapper.selectByPrimaryKey(delayOrder.getId());
                if (order.getState().equals(0)) {
                    order.setCanTime(TimeUtils.getNowTime());
                    order.setState(2);
                    zOrderMapper.updateByPrimaryKeySelective(order);
                    //rabbitmqService.canOrder(order);
                    logger.info("订单超时未支付，已被取消，订单号: {}", order.getId());
                } else if (order.getState().equals(1)) {
                    logger.info("订单已完成支付，订单号: {}", order.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("订单消费失败，请查看原因: {}", e.getCause());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("消息转码失败，请查看原因", e.getCause());
        }
    }

}
