package com.company.dao;

import com.company.domain.UserWork;
import org.apache.ibatis.annotations.*;
import java.util.Date;
import java.util.List;

//work_user
public interface IUserWorkDao {
    /**
     * 查询学生作业信息
     * @return
     */
    @Select("select * from work_user")
    List<UserWork> findAll();
    /**
     * 插入一条作业信息
     * @param userWork
     */
    @Insert("insert into work_user(studentID,date,status,work) values(#{studentID},#{date},#{status},#{work})")
    void insert(UserWork userWork);

    /**
     * 更新日期
     * @param date
     */
    @Update("update work_user set date=#{date} where studentID=#{studentID} and work=#{work}")
    void updateDate(@Param("date") Date date, @Param("studentID") String studentID,@Param("work") String work);

    /**
     * 更新审核状态
     * @param status
     */
    @Update("update work_user set status=#{status} where studentID=#{studentID} and work=#{work}")
    void updateStatus(@Param("status") String status,@Param("studentID") String studentID,@Param("work") String work);

    /**
     * 将审核变为无效
     * @param status
     * @param work
     */
    @Update("update work_user set status=#{status} where work=#{work}")
    void delete_Status(@Param("status") String status,@Param("work") String work);

    /**
     * 查找作者作业信息
     * @param studentID
     * @return
     */
    @Select("select * from work_user where studentID=#{studentID}")
    List<UserWork> findByStudentID(String studentID);

    /**
     * 查找某作业提交信息
     * @param work
     * @return
     */
    @Select("select * from work_user where work=#{work}")
    List<UserWork> findByWork(String work);


    /**
     * 查找某作业提交信息 一条
     * @param work
     * @return
     */
    @Select("select * from work_user where work=#{work} and studentID=#{studentID}")
    UserWork findWork(@Param("work") String work,@Param("studentID") String studentID);

    /**
     * 删除无效作业
     * @param studentID
     * @param status
     */
    @Delete("delete from work_user where studentID=#{studentID} and status=#{status}")
    void deleteAllByStatus(@Param("studentID") String studentID,@Param("status") String status);

    /**
     * 批量删除
     * @param studentID
     */
    @Delete("delete from work_user where studentID=#{studentID}")
    void deleteAll(@Param("studentID") String studentID);


    @Select("select * from work_user where studentID=#{studentID} and status=#{status}")
    List<UserWork> selectInvalidUserWork(@Param("studentID") String studentID,@Param("status") String status);

    /**
     * 删除一条作业信息
     * @param work
     */
    @Delete("delete from work_user where work=#{work} and studentID=#{studentID}")
    void delete(@Param("work") String work,@Param("studentID") String studentID);

    /**
     * 查找提交人数
     * @return
     */
    @Select("select count(id) from work_user where work=#{work}")
    int count(String work);
}
