package com.seckill.demo.demo.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class ZOrder {
    /**
     *   
     */
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    /**
     *   
     */
    @JsonProperty("member_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer memberId;

    /**
     *   
     */
    @JsonProperty("member_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String memberName;

    /**
     *   
     */
    @JsonProperty("num")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String num;

    /**
     *  0 待支付 1 已支付 2 已取消 3 已删除 
     */
    @JsonProperty("state")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer state;

    /**
     *   
     */
    @JsonProperty("add_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer addTime;

    /**
     *   
     */
    @JsonProperty("can_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer canTime;

    /**
     *   
     */
    @JsonProperty("price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal price;

    /**
     *   
     */
    @JsonProperty("goods_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer goodsId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName == null ? null : memberName.trim();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getCanTime() {
        return canTime;
    }

    public void setCanTime(Integer canTime) {
        this.canTime = canTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id+ "," + "\"memberId\":" + memberId+ "," + "\"memberName\":" + memberName+ "," + "\"num\":" + num+ "," + "\"state\":" + state+ "," + "\"addTime\":" + addTime+ "," + "\"canTime\":" + canTime+ "," + "\"price\":" + price+ "," + "\"goodsId\":" + goodsId+  "}";
    }
}