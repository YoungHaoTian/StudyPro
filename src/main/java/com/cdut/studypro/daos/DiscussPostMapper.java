package com.cdut.studypro.daos;

import com.cdut.studypro.beans.DiscussPost;
import com.cdut.studypro.beans.DiscussPostExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DiscussPostMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    long countByExample(DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int deleteByExample(DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int insert(DiscussPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int insertSelective(DiscussPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    List<DiscussPost> selectByExampleWithBLOBs(DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    List<DiscussPost> selectByExample(DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    DiscussPost selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int updateByExampleSelective(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int updateByExampleWithBLOBs(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int updateByExample(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int updateByPrimaryKeySelective(DiscussPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(DiscussPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table discuss_post
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    int updateByPrimaryKey(DiscussPost record);
}