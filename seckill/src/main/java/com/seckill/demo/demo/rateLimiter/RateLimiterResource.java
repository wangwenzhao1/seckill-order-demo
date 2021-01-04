package com.seckill.demo.demo.rateLimiter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seckill.demo.demo.util.DataResponse;


@RestController
@RequestMapping(value = "/rateLimiter/v1")
public class RateLimiterResource {


    @PostMapping(value = "/rateLimiter")
    @ExtRateLimiter(permitsPerSecond = 2, timeout = 500)
    public DataResponse rateLimiter(@RequestBody String string) throws Exception {
            System.out.println("抢购 成功了 --------------");
            return DataResponse.getInstance();
    }

    @PostMapping(value = "/rateLimiter01")
    @ExtRateLimiter(permitsPerSecond = 1, timeout = 500)
    public DataResponse rateLimiter01(@RequestBody String string) throws Exception {
            System.out.println("抢购 成功了11 --------------");
            return DataResponse.getInstance();
    }

    @PostMapping(value = "/rateLimiter02")
    @ExtRateLimiter(permitsPerSecond = 0.5, timeout = 100)
    public DataResponse rateLimiter02(@RequestBody String string) throws Exception {
            System.out.println("抢购 成功了02 --------------");
            return DataResponse.getInstance();
    }


}
