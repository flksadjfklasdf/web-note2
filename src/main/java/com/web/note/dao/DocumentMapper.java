package com.web.note.dao;

import com.web.note.entity.Document;
import com.web.note.entity.DocumentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DocumentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int countByExample(DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int deleteByExample(DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int deleteByPrimaryKey(String documentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int insert(Document record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int insertSelective(Document record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    List<Document> selectByExampleWithBLOBs(DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    List<Document> selectByExample(DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    Document selectByPrimaryKey(String documentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int updateByExampleSelective(@Param("record") Document record, @Param("example") DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int updateByExampleWithBLOBs(@Param("record") Document record, @Param("example") DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int updateByExample(@Param("record") Document record, @Param("example") DocumentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int updateByPrimaryKeySelective(Document record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int updateByPrimaryKeyWithBLOBs(Document record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table document
     *
     * @mbggenerated Thu Sep 05 20:28:14 CST 2024
     */
    int updateByPrimaryKey(Document record);

    List<Document> getDocumentsByUserIdHavingImage(String userId, String collectionId);

    int getMaxOrder(String collectionId);
}