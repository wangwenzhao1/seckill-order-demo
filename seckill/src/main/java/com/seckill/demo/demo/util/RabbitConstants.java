package com.seckill.demo.demo.util;

/**
 * @ClassName: RabbitConstants
 * @author wangwenzhao
 * @date 2018年12月4日下午3:42:21
 * @version: V1.0
 */
public class RabbitConstants {

    public static final String SCF_MESSAGE = "scf_message_notice";


    /**
     * 订单消息实际消费交换机，队列,路由
     */
    public final static String EXCHANGE_NAME = "order_exchange";
    public final static String QUEUE_NAME = "order_queue";
    public final static String ROUTE_KEY = "order_routing_key";
    /**
     * 订单消息延迟消费队列所绑定的交换机
     */
    public final static String DELAY_EXCHANGE_NAME = "order_delay_exchange";
    public final static String DELAY_QUEUE_NAME = "order_delay_queue";
    public final static String DEALY_ROUTE_KEY = "order_delay_routing_key";

}
