package com.company;

import com.company.dao.ITeacherDao;
import com.company.domain.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class TeacherCRUDTest {
    private InputStream in;
    private SqlSessionFactory sessionFactory;
    private SqlSession sqlSession;
    private ITeacherDao iTeacherDao;

    /**
     * 初始化方法
     * @throws Exception
     */
    @Before
    public void init() throws Exception{
        //1.读取配置文件
        in = Resources.getResourceAsStream("SqlMapConfig");
        //2.创建SQLSessionFactory工厂
        sessionFactory = new SqlSessionFactoryBuilder().build(in);
        //3.使用工厂创建sqlSession对象
        sqlSession = sessionFactory.openSession();
        //4.使用sqlSession创建dao代理对象
        iTeacherDao = sqlSession.getMapper(ITeacherDao.class);
    }

    /**
     * 结束方法
     * @throws Exception
     */
    @After
    public void destroy() throws Exception{
        sqlSession.close();
        in.close();
    }

    /**
     * 查找全部
     */
    @Test
    public void findAll(){
       List<Teacher> m = iTeacherDao.findAll();
       for(Teacher t:m){
           System.out.println(t);
       }

    }
    /**
     * 插入一条数据
     */
    @Test
    public void testInsert(){
        Teacher m = new Teacher();
        m.setWorkID("112");
        m.setPassword("112");
        m.setName("sww");
        m.setTel("13194476288");
        m.setType("java");
        iTeacherDao.insert(m);
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
        findAll();
    }

    /**
     * 更新操作
     */
    @Test
    public void testUpdate(){
        iTeacherDao.updateType("Java","112");
        //iManagerDao.updateName("雷竣杰",1);
        //iManagerDao.updatePassword("1122",1);
        //iTeacherDao.updateWorkID("1111",1);
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
    }
    @Test
    public void testDelete(){
        Date d = new Date();
        System.out.println(Work.getDate(d));
    }
    @Test
    public void testCount(){
       // int n = iTeacherDao.count();
       // System.out.println(n);
    }
    @Test
    public void testFind(){
        Teacher u = iTeacherDao.find("司维维");
        System.out.println(u);
    }
}
