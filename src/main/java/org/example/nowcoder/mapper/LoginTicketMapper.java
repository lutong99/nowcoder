package org.example.nowcoder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nowcoder.entity.LoginTicket;
import org.example.nowcoder.entity.LoginTicketExample;

import java.util.List;

@Mapper
@Deprecated
public interface LoginTicketMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int countByExample(LoginTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int deleteByExample(LoginTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int insert(LoginTicket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int insertSelective(LoginTicket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    List<LoginTicket> selectByExample(LoginTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    LoginTicket selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int updateByExampleSelective(@Param("record") LoginTicket record, @Param("example") LoginTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int updateByExample(@Param("record") LoginTicket record, @Param("example") LoginTicketExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int updateByPrimaryKeySelective(LoginTicket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_ticket
     *
     * @mbggenerated Sat Aug 12 21:31:59 CST 2023
     */
    int updateByPrimaryKey(LoginTicket record);
}