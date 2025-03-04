package com.web.note.dao;

import com.web.note.entity.FileResource;
import com.web.note.entity.FileResourceExample;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface FileResourceMapper {
    int countByExample(FileResourceExample example);

    int deleteByExample(FileResourceExample example);

    int deleteByPrimaryKey(String fileId);

    int insert(FileResource record);

    int insertSelective(FileResource record);

    List<FileResource> selectByExample(FileResourceExample example);

    FileResource selectByPrimaryKey(String fileId);

    int updateByExampleSelective(@Param("record") FileResource record, @Param("example") FileResourceExample example);

    int updateByExample(@Param("record") FileResource record, @Param("example") FileResourceExample example);

    int updateByPrimaryKeySelective(FileResource record);

    int updateByPrimaryKey(FileResource record);

    int countFileSizeByUser(@Param("userId") String userId);
    List<FileResource> selectFiles(String userId, String fileName, String fileType, Date startTime, Date endTime, Integer minSize, Integer maxSize, Integer pageSize, Integer offset);

    int countByUserId(String userId, String fileName, String fileType, Date startTime, Date endTime, Integer minSize, Integer maxSize);
}