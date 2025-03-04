package com.web.note.dao;

import com.web.note.entity.UserConfig;
import com.web.note.entity.UserConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserConfigMapper {
    
    int countByExample(UserConfigExample example);

    
    int deleteByExample(UserConfigExample example);

    
    int deleteByPrimaryKey(String configId);

    
    int insert(UserConfig record);

    
    int insertSelective(UserConfig record);

    
    List<UserConfig> selectByExample(UserConfigExample example);

    
    UserConfig selectByPrimaryKey(String configId);

    
    int updateByExampleSelective(@Param("record") UserConfig record, @Param("example") UserConfigExample example);

    
    int updateByExample(@Param("record") UserConfig record, @Param("example") UserConfigExample example);

    
    int updateByPrimaryKeySelective(UserConfig record);

    
    int updateByPrimaryKey(UserConfig record);
}