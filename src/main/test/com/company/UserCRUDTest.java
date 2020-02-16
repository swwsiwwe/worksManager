package com.company;

import com.company.dao.IUserDao;
import com.company.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;

import java.util.List;

public class UserCRUDTest {
    private InputStream in;
    private SqlSessionFactory sessionFactory;
    private SqlSession sqlSession;
    private IUserDao iUserDao;

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
        iUserDao = sqlSession.getMapper(IUserDao.class);
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
        List<User> l = iUserDao.findAll();
        for(User u:l){
            System.out.println(u);
        }
    }
    /**
     * 插入一条数据
     */
    @Test
    public void testInsert(){
        User u = new User();
        u.setName("雷竣杰");
        u.setStudentID("123");
        u.setPassword("123");
        u.setType("java");
        u.setLevel(18);
        iUserDao.insert(u);
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
    }

    /**
     * 更新操作
     * ok
     */
    @Test
    public void testUpdate(){
        //iUserDao.updateLevel(17,1);
        //iUserDao.updateName("杰","2211234");
       //iUserDao.updatePassword("12313","2211234");
        //iUserDao.updateType("c++","2211234");
       //iUserDao.updateLevel(17,"2211234");
        iUserDao.updateType("Java","123");
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
        findAll();
    }
    @Test
    public void testDelete(){
        //String name="司维维";
        iUserDao.deleteByStudentID("2211234");
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
        findAll();
    }
    @Test
    public void testCount(){
        int n = iUserDao.count();
        System.out.println(n);
    }
    @Test
    public void testFind(){
        //User u = iUserDao.findByName("司维维");
        //System.out.println(u);
    }
}
