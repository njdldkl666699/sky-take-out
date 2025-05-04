package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     *
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenID(String openid);

    /**
     * 插入新用户
     *
     * @param user
     */
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time)" +
            "values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    /**
     * 通过用户id查询用户信息
     *
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);

    /**
     * 通过时间范围统计用户数量
     *
     * @param begin
     * @param end
     * @return
     */
    Integer countByTimeRange(LocalDateTime begin, LocalDateTime end);
}
