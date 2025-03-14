package com.web.note.dao;

import com.web.note.entity.UserSession;
import com.web.note.entity.UserSessionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserSessionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int countByExample(UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int deleteByExample(UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int deleteByPrimaryKey(String sessionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int insert(UserSession record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int insertSelective(UserSession record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    List<UserSession> selectByExampleWithBLOBs(UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    List<UserSession> selectByExample(UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    UserSession selectByPrimaryKey(String sessionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int updateByExampleSelective(@Param("record") UserSession record, @Param("example") UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int updateByExampleWithBLOBs(@Param("record") UserSession record, @Param("example") UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int updateByExample(@Param("record") UserSession record, @Param("example") UserSessionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int updateByPrimaryKeySelective(UserSession record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int updateByPrimaryKeyWithBLOBs(UserSession record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table session
     *
     * @mbggenerated Thu Apr 18 19:27:16 CST 2024
     */
    int updateByPrimaryKey(UserSession record);
}