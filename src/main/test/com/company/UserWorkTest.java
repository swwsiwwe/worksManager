package com.company;

import com.company.dao.IUserWorkDao;
import com.company.domain.User;
import com.company.domain.UserWork;
import com.company.service.UserService;
import com.company.service.UserWorkService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import java.util.List;


public class UserWorkTest {
    private InputStream in;
    private SqlSessionFactory sessionFactory;
    private SqlSession sqlSession;
    private IUserWorkDao dao;

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
        dao = sqlSession.getMapper(IUserWorkDao.class);
    }

    @After
    public void destroy() throws Exception{
        sqlSession.close();
        in.close();
    }

    /**
     * 查找全部
     */
    @Test
    public void findAll() {
        //List<UserWork> l = dao.findAll();
        List<UserWork> l = UserService.getMyWorks("123");
        for(UserWork u:l){
            System.out.println(u);
        }//##User{id=2, name='司维维', studentID='123', password='123123', type='Java', level=18}
    }

    /**
     * 插入一条数据
     */
    @Test
    public void testInsert(){
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
    }
    @Test
    public void testUpdate(){
        sqlSession.commit();
    }
    @Test
    public void testDelete(){
        dao.deleteAll("123");
        //dao.delete_Status();
        //事务提交
        //不提交mybatis将会回滚
        sqlSession.commit();
    }
    @Test
    public void testCount(){
    }
    @Test
    public void testFind(){
    }
}
