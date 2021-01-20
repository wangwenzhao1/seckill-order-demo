package com.seckill.demo.demo.util;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 返回结构定义
 * @ClassName: DataResponse
 * @author wangwenzhao
 * @date Jan 20, 20213:27:50 PM
 * @version: V1.0
 */
public class DataResponse {

    private DataResponse() {

    }

    private DataResponse(Integer errorCode,String message, Object resultData) {
        this.errorCode = errorCode;
        this.errorMessage = message;
        this.resultData = resultData;
    }

    private DataResponse(Integer errorCode,String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    private DataResponse(Object resultData) {
        this.resultData = resultData;
    }

    public static DataResponse getInstance() {
        return new DataResponse(200, "服务调用成功");
    }

    public static DataResponse getInstance(Object resultData) {
        resultData = packResult(resultData);
        return new DataResponse(200, "服务调用成功", resultData);
    }

    @JsonProperty("error_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;
    @JsonProperty("error_message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;
    @JsonProperty("result_data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object resultData;

    private static Object packResult(Object resultData) {
        if (resultData != null) {
            Object tempo = resultData;
            if (resultData instanceof List || resultData.getClass().isArray()) {
                tempo = new HashMap<String, Object>() {
                    {
                        put("items", resultData);
                    }
                };
            }
            return tempo;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "{" + "\"errorCode\": " + errorCode + "," + "\"resultData\": " + resultData + "\"," + "}";
    }
}
