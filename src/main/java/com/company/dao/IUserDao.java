package com.company.dao;

import com.company.domain.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface IUserDao {
    /**
     * 查询所有操作
     * @return
     */
    @Select("select * from user")
    List<User> findAll();
    /**
     * 插入一条学生信息
     * @param user
     */
    @Insert("insert into user(name,studentID,password,type,level) values(#{name},#{studentID},#{password},#{type},#{level})")
    void insert(User user);

    /**
     * 更新姓名
     * @param name
     */
    @Update("update user set name=#{name} where studentID=#{studentID}")
    void updateName(@Param("name") String name,@Param("studentID") String studentID);

    /**
     * 更新学号
     * @param studentID
     */
//    @Update("update user set studentID=#{studentID} where id=#{id}")
//    void updateStudentID(@Param("studentID")String studentID,@Param("id") int id);

    /**
     * 更新密码
     * @param password
     */
    @Update("update user set password=#{password} where studentID=#{studentID}")
    void updatePassword(@Param("password") String password,@Param("studentID") String studentID);

    /**
     * 更新类别
     * @param type
     */
    @Update("update user set type=#{type} where studentID=#{studentID}")
    void updateType(@Param("type") String type,@Param("studentID") String studentID);

    /**
     * 更新年级
     * @param level
     */
    @Update("update user set level=#{level} where studentID=#{studentID}")
    void updateLevel(@Param("level") Integer level,@Param("studentID")String studentID);

    /**
     * 根据学号查找信息
     * @param studentID
     * @return
     */
    @Select("select * from user where studentID=#{studentID}")
    User findByStudentID(String studentID);


    /**
     * 删除
     * @param studentID
     */
    @Delete("delete from user where studentID=#{studentID}")
    void deleteByStudentID(String studentID);

    /**
     * 查找表行数
     * @return
     */
    @Select("select count(id) from user")
    int count();
}
