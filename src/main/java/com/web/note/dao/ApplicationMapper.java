package com.web.note.dao;

import com.web.note.entity.Application;
import com.web.note.entity.ApplicationExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ApplicationMapper {

    int countByExample(ApplicationExample example);

    int deleteByExample(ApplicationExample example);

    int deleteByPrimaryKey(String appId);

    int insert(Application record);

    int insertSelective(Application record);

    List<Application> selectByExampleWithBLOBs(ApplicationExample example);

    List<Application> selectByExample(ApplicationExample example);

    Application selectByPrimaryKey(String appId);

    int updateByExampleSelective(@Param("record") Application record, @Param("example") ApplicationExample example);

    int updateByExampleWithBLOBs(@Param("record") Application record, @Param("example") ApplicationExample example);

    int updateByExample(@Param("record") Application record, @Param("example") ApplicationExample example);

    int updateByPrimaryKeySelective(Application record);

    int updateByPrimaryKeyWithBLOBs(Application record);

    int updateByPrimaryKey(Application record);

    List<Application> searchApplicationsByParam(Map<String, Integer> params);

    Integer countApplications(Map<String, Integer> params);
}