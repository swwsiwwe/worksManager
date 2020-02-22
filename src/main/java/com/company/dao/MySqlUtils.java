package com.company.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.InputStream;

public class MySqlUtils {
    private InputStream in;
    private SqlSessionFactory sessionFactory;
    private SqlSession sqlSession;
    private ITeacherDao iTeacherDao;
    private IWorkDao iWorkDao;
    private IUserWorkDao iUserWorkDao;
    private IUserDao iUserDao;
    public void init(){
        try{
            //1.读取配置文件
            in = Resources.getResourceAsStream("SqlMapConfig");
            //2.创建SQLSessionFactory工厂
            sessionFactory = new SqlSessionFactoryBuilder().build(in);
            //3.使用工厂创建sqlSession对象
            sqlSession = sessionFactory.openSession();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void commit(){
        sqlSession.commit();
    }
    public ITeacherDao getITeacherDao(){
        return sqlSession.getMapper(ITeacherDao.class);
    }
    public IUserDao getIUserDao(){
        return sqlSession.getMapper(IUserDao.class);
    }
    public IWorkDao getIWorkDao(){
        return sqlSession.getMapper(IWorkDao.class);
    }
    public IUserWorkDao getIUserWorkDao(){
        return sqlSession.getMapper(IUserWorkDao.class);
    }

    public void close(){
        try{
            sqlSession.close();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}