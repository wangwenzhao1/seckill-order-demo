package com.seckill.demo.demo.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seckill.demo.demo.entry.OrderVO;
import com.seckill.demo.demo.entry.ZOrder;
import com.seckill.demo.demo.mapper.ZOrderMapper;
import com.seckill.demo.demo.util.DataResponse;
import com.seckill.demo.demo.util.TimeUtils;

/**
 * <p>
 * Description:
 * </p>
 *
 * @ClassName: RabbitmqService
 * @author wangwenzhao
 * @date Sep 7, 20203:29:39 PM
 * @version: V1.0
 */
@Service
public class RabbitmqService {

    @Autowired
    RabbitService rabbitService;

    @Autowired
    ZOrderMapper zOrderMapper;

    /**
     * 添加订单
     * @param orderVO
     * @return
     */
    public DataResponse addorder(OrderVO orderVO) {

       String num = String.format("20%s%d","OX-",System.currentTimeMillis() / 1000);

        orderVO.setNum(num);
        orderVO.setGoodsId(orderVO.getGoodsId());
        orderVO.setAddTime(TimeUtils.getNowTime());
        orderVO.setMemberId(orderVO.getMemberId());
        orderVO.setMemberName("wangzhi延迟消费");
        orderVO.setPrice(new BigDecimal(98.2));
        orderVO.setState(0);
        zOrderMapper.insert(orderVO);
        System.out.println("订单创建成功，订单号: {}" + orderVO.getId());
        //添加到消息队列
        rabbitService.sendDelay(orderVO, 5 * 60 * 1000L);
        System.out.println("订单已发送致延时队列，请尽快支付");
        return DataResponse.getInstance();
    }

    /**
     * 取消订单
     * @author wangwenzhao
     * @param orderVO
     * @return
     */
    public DataResponse canOrder(ZOrder orderVO){
        orderVO.setCanTime(TimeUtils.getNowTime());
        orderVO.setState(2);
        zOrderMapper.updateByPrimaryKeySelective(orderVO);
        return DataResponse.getInstance();
    }


    public static void main(String[] args) {
        String num = String.format("20%s%d","ZX-",System.currentTimeMillis() / 1000);
        System.out.println(num);
    }

}
