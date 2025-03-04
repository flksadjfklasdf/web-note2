package com.web.note.dao;

import com.web.note.entity.Collection;
import com.web.note.entity.CollectionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CollectionMapper {
    int countByExample(CollectionExample example);
    int deleteByExample(CollectionExample example);
    int deleteByPrimaryKey(String collectionId);
    int insert(Collection record);
    int insertSelective(Collection record);
    List<Collection> selectByExampleWithBLOBs(CollectionExample example);
    List<Collection> selectByExample(CollectionExample example);
    Collection selectByPrimaryKey(String collectionId);
    int updateByExampleSelective(@Param("record") Collection record, @Param("example") CollectionExample example);
    int updateByExampleWithBLOBs(@Param("record") Collection record, @Param("example") CollectionExample example);
    int updateByExample(@Param("record") Collection record, @Param("example") CollectionExample example);
    int updateByPrimaryKeySelective(Collection record);
    int updateByPrimaryKeyWithBLOBs(Collection record);
    int updateByPrimaryKey(Collection record);

    List<Collection> getAllCollectionByUserIdHavingImage(String userId);
}