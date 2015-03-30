package com.pantuo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pantuo.dao.entity.Contract;
import com.pantuo.dao.entity.ContractExample;

public interface ContractMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int countByExample(ContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int deleteByExample(ContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int insert(Contract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int insertSelective(Contract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    List<Contract> selectByExample(ContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    Contract selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Contract record, @Param("example") ContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Contract record, @Param("example") ContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Contract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table contract
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Contract record);
}