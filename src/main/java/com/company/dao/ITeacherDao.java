package com.company.dao;

import com.company.domain.Teacher;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ITeacherDao {

    /**
     * 查询所有操作
     * @return
     */
    @Select("select * from teacher")
    List<Teacher> findAll();

    /**
     * 插入一条教师信息
     * @param teacher
     */
    @Insert("insert into teacher(name,tel,type,workID,password) values(#{name},#{tel},#{type},#{workID},#{password})")
    void insert(Teacher teacher);

    /**
     * 更新姓名
     * @param name
     */
    @Update("update teacher set name=#{name} where workID=#{workID}")
    void updateName(@Param("name") String name, @Param("workID") String workID);

    /**
     * 更新联系方式
     * @param type
     */
    @Update("update teacher set type=#{type} where workID=#{workID}")
    void updateType(@Param("type") String type, @Param("workID") String workID);
    /**
     * 更新联系方式
     * @param tel
     */
    @Update("update teacher set tel=#{tel} where workID=#{workID}")
    void updateTel(@Param("tel") String tel, @Param("workID") String workID);

    /**
     * 更新工号
     * @param workID
     */
//    @Update("update manager set workID=#{workID} where id=#{id}")
//    void updateWorkID(@Param("workID")String workID,@Param("id") int id);

    /**
     * 更新密码
     * @param password
     */
    @Update("update teacher set password=#{password} where workID=#{workID}")
    void updatePassword(@Param("password") String password,@Param("workID") String workID);

    /**
     * 根据工号查找信息
     * @param workID
     * @return
     */
    @Select("select * from teacher where workID=#{workID}")
    Teacher find(String workID);

    /**
     * 删除
     * @param workID
     */
    @Delete("delete from teacher where workID=#{workID}")
    void delete(String workID);

    /**
     * 查找表行数
     * @return
     */
//    @Select("select count(id) from manager")
//    int count();
}
