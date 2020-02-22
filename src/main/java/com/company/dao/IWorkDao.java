package com.company.dao;

import com.company.domain.Work;
import org.apache.ibatis.annotations.*;
import java.util.Date;
import java.util.List;

public interface IWorkDao {

    /**
     * 插入一条作业信息
     * @param work
     */
    @Insert("insert into work(name,work,workID,start,end,level,type) values(#{name},#{work},#{workID},#{start},#{end},#{level},#{type})")
    @SelectKey(statement = "select last_insert_id()" ,keyProperty = "id",keyColumn = "id",resultType = int.class,before = false)
    int insert(Work work);

    /**
     * 修改考核名字
     * @param name
     * @param work
     */
    @Update("update work set name=#{name} where work=#{work}")
    void updateName(@Param("name") String name, @Param("work") String work);

    /**
     * 更新发布时间
     * @param start
     */
    @Update("update work set start=#{start} where work=#{work}")
    void updateStart(@Param("start") Date start, @Param("work") String work);

    /**
     * 更新截止时间
     * @param end
     */
    @Update("update work set end=#{end} where work=#{work}")
    void updateEnd(@Param("end") Date end, @Param("work") String work);

    /**
     * 设置标识码
     * @param work
     * @param id
     */
    @Update("update work set work=#{work} where id=#{id}")
    void updateWork(@Param("work") String work,@Param("id") int id);

    /**
     * 查找作业信息
     * @param workID
     * @return
     */
    @Select("select * from work where workID=#{workID}")
    List<Work> find(String workID);

    /**
     * 根据标识码查找作业信息
     * @param work
     * @return
     */
    @Select("select * from work where work=#{work}")
    Work findByWork(String work);

    /**
     *根据种类和年级查询作业
     * @param type
     * @param level
     * @return
     */
    @Select("select * from work where type=#{type} and level=#{level}")
    List<Work> findByUser(@Param("type") String type,@Param("level") int level);


    /**
     * 删除作业
     * @param work
     */
    @Delete("delete from work where work=#{work}")
    void delete(String work);

    /**
     * 删除管理员下的全部作业
     * @param workID
     */
    @Delete("delete from work where workID=#{workID}")
    void deleteAll(String workID);
}