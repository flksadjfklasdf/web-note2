package com.web.note.dao;

import com.web.note.entity.AppConfig;
import com.web.note.entity.AppConfigExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppConfigMapper {
    int countByExample(AppConfigExample example);

    int deleteByExample(AppConfigExample example);

    int deleteByPrimaryKey(String configItem);

    int insert(AppConfig record);

    int insertSelective(AppConfig record);

    List<AppConfig> selectByExample(AppConfigExample example);

    AppConfig selectByPrimaryKey(String configItem);

    int updateByExampleSelective(@Param("record") AppConfig record, @Param("example") AppConfigExample example);

    int updateByExample(@Param("record") AppConfig record, @Param("example") AppConfigExample example);

    int updateByPrimaryKeySelective(AppConfig record);

    int updateByPrimaryKey(AppConfig record);
}