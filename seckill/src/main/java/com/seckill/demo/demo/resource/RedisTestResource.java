package com.seckill.demo.demo.resource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.seckill.demo.demo.entry.OrderVO;
import com.seckill.demo.demo.entry.ZOrder;
import com.seckill.demo.demo.entry.ZOrderCriteria;
import com.seckill.demo.demo.mapper.ZOrderMapper;
import com.seckill.demo.demo.rateLimiter.ExtRateLimiter;
import com.seckill.demo.demo.service.RabbitmqService;
import com.seckill.demo.demo.util.DataResponse;
import com.seckill.demo.demo.util.RedisTool;

/**
 * 测试 Controller
 * @ClassName: RedisTestResource
 * @author wangwenzhao
 * @date Sep 11, 202010:58:43 AM
 * @version: V1.0
 */

@RestController
@RequestMapping(value = "/seckill/v1")
public class RedisTestResource {

    RedisTool redisTool;

    @Autowired
    public RedisTestResource(RedisTool redisTool) {
        this.redisTool = redisTool;
    }

    @Autowired
    ZOrderMapper zOrderMapper;

    @Autowired
    RabbitmqService rabbitmqService;

    /**
     * 商品秒杀； 限流 + 服务降级 + redis.decr预减库存，+ rabbitmq减库存+生成订单+死信队列 取消订单处理 + db减库存
     * @author wangwenzhao
     * @param inta
     * @param request
     * @return
     */
    @PostMapping(value = "/productSeckill/seckill")
    @ExtRateLimiter(permitsPerSecond = 10, timeout = 300)
    public DataResponse productSeckill(HttpServletRequest request){

        //todo
        ////可以增加 令牌验证判断是否是正常的抢购，防止刷单。
        //操作 减库存 可以调整为 消息队列，设置监听去 db 添加记录，等业务处理。

        OrderVO orderVO = new OrderVO();
        System.out.println("xinx : " +request.getParameter("goods_id"));
        orderVO.setGoodsId(Integer.parseInt(request.getParameter("goods_id")));
        orderVO.setMemberId(Integer.parseInt(request.getParameter("member_id")));
        System.out.println("用户：" + JSONObject.toJSON(orderVO));

        //预减库存
        long stock = redisTool.decr("goods_id" + orderVO.getGoodsId().toString());
        if (stock < 0) {
            System.out.println("-------------------商品已经秒杀完---------");
           return DataResponse.getInstance("---商品已经秒杀完------");
        }
        //判断是否是重复抢购。
        ZOrderCriteria zOrderCriteria = new ZOrderCriteria();
        ZOrderCriteria.Criteria criteria = zOrderCriteria.createCriteria();
        criteria.andMemberIdEqualTo(orderVO.getMemberId());
        criteria.andGoodsIdEqualTo(orderVO.getGoodsId());
        List<ZOrder> list = zOrderMapper.selectByCriteria(zOrderCriteria);
        if(!CollectionUtils.isEmpty(list)){
            System.out.println("------不能重复抢购！！！--------");
            return DataResponse.getInstance("---不能重复抢购！！！------");
        }
        //mq
        rabbitmqService.addorder(orderVO);

        return DataResponse.getInstance();
    }

    /**
     * 初始化1000 个抢购商品 每个 1000 件。
     * @param inta
     * @param request
     * @return
     */
    @PostMapping(value = "/productini")
    public DataResponse productIni(@RequestBody Integer inta, HttpServletRequest request){
        //初始化 1000个商品 抢购。
        for (int i = 1 ; i <= 100; i++) {
            redisTool.set("goods_id" + i, 1000+"");
        }
        return DataResponse.getInstance();
    }

    @PostMapping(value = "/test")
    public DataResponse test(@RequestBody Integer inta, HttpServletRequest request){
        //初始化 1000个商品 抢购。
        for (int i = 1 ; i <= 10; i++) {
            System.out.println("------test------");
        }
        return DataResponse.getInstance();
    }




    @PostMapping(value = "/addredist/list")
    public DataResponse addredist(@RequestBody Integer inta, HttpServletRequest request)
            throws IOException {

        Long id = redisTool.incr("te_ins");

        redisTool.hset("ins_id_" + id, "id", id.toString());
        redisTool.hset("ins_id_" + id, "name", "woro");
        redisTool.hset("ins_id_" + id, "age", "18");


        System.out.println(redisTool.hget("ins_id_" + id, "id"));
        System.out.println(redisTool.hget("ins_id_" + id, "name"));
        System.out.println(redisTool.hget("ins_id_" + id, "age"));

        System.out.println("-----------------hset");

        redisTool.lpush("shujukun", "mysql");
        redisTool.lpush("shujukun", "redis");
        redisTool.lpush("shujukun", "memcache");
        redisTool.lpush("shujukun", "mongdb");


        List<String> shuj = redisTool.blpop("shujukun");
        System.out.println(shuj.toString());
        System.out.println("-----------------list");

        redisTool.lpush("xingm", "d");
        redisTool.lpush("xingm", "reddfdis");
        redisTool.lpush("xingm", "geeeeeeeeee");
        redisTool.lpush("xingm", "degegeg");
        shuj = redisTool.brpop("xingm");

        System.out.println(shuj.toString());
        System.out.println("------------  list to");

        redisTool.set("wang", "wemndljl");
        redisTool.set("liu", "gouzi");
        redisTool.set("zhang", "magiz");


        redisTool.sadd("maptest","map01","map02","map03");

        Set<String> set = redisTool.sem("maptest");
        System.out.println(set.toString());

        System.out.println("---------------set");


        redisTool.zset("zsetmap" , 1 , "z01");
        redisTool.zset("zsetmap" , 2 , "z02ddd");
        redisTool.zset("zsetmap" , 4 , "z04ssss");
        redisTool.zset("zsetmap" , 3 , "z03dfde");

        Set<String> sz = redisTool.zrange("zsetmap" , 0, 10);
        System.out.println(sz.toString());

        return DataResponse.getInstance();
    }

}
