package com.seckill.demo.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.seckill.demo.demo.entry.ZOrder;
import com.seckill.demo.demo.entry.ZOrderCriteria;

public interface ZOrderMapper {
    long countByCriteria(ZOrderCriteria criteria);

    int deleteByCriteria(ZOrderCriteria criteria);

    int deleteByPrimaryKey(Integer id);

    int insert(ZOrder record);

    int insertSelective(ZOrder record);

    List<ZOrder> selectByCriteria(ZOrderCriteria criteria);

    ZOrder selectByPrimaryKey(Integer id);

    int updateByCriteriaSelective(@Param("record") ZOrder record, @Param("example") ZOrderCriteria criteria);

    int updateByCriteria(@Param("record") ZOrder record, @Param("example") ZOrderCriteria criteria);

    int updateByPrimaryKeySelective(ZOrder record);

    int updateByPrimaryKey(ZOrder record);

    void insertBatch(List<ZOrder> recordLst);
}