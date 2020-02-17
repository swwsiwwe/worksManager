package com.company;

import com.company.dao.IWorkDao;
import com.company.domain.Work;
import com.company.service.WorkService;
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

public class WorkTest {
    private InputStream in;
    private SqlSessionFactory sessionFactory;
    private SqlSession sqlSession;
    private IWorkDao dao;

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
        dao = sqlSession.getMapper(IWorkDao.class);
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
        List<Work> m = dao.find("112");
       for(Work u:m){
           System.out.println(u);
        }
    }
    /**
     * 插入一条数据
     */
    @Test
    public void testInsert(){
        Work work =  new Work();
        work.setWork("Java18");
        work.setStart(new Date());
        work.setWorkID("112");
        work.setEnd(new Date());
        work.setName("11");
        work.setLevel(18);
        work.setType("Java");
        System.out.println(work.getId());
        WorkService.insertWork(work);
        System.out.println(work.getId());
        sqlSession.commit();
        findAll();

    }

    /**
     * 更新操作
     */
    @Test
    public void testUpdate(){
        //iManagerDao.updateName("雷竣杰",1);
        //iManagerDao.updatePassword("1122",1);

        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
    }
    @Test
    public void testDelete(){
        String name="司维维";
dao.deleteAll("112");
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
    }
    @Test
    public void testCount(){

    }
    @Test
    public void testFind(){
        List<Work> u = dao.find("司维维");
        System.out.println(u);
    }
}
