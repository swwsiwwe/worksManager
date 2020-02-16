package com.company.service;

import com.company.dao.ITeacherDao;
import com.company.dao.IUserWorkDao;
import com.company.dao.MySqlUtils;
import com.company.domain.Teacher;
import com.company.domain.UserWork;
import java.util.List;

public class TeacherService {

    /**
     * 查找教师信息
     * @param workID
     * @return
     */
    public static Teacher findByWorkID(String workID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        Teacher m = iTeacherDao.find(workID);
        MySqlUtils.close();
        return m;
    }

    /**
     * 修改密码
     * @param password
     * @param workID
     */
    public static void updatePassword(String password,String workID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        iTeacherDao.updatePassword(password, workID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    /**
     * 改名字
     * @param name
     * @param workID
     */
    public static void updateName(String name,String workID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        iTeacherDao.updateName(name, workID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void updateType(String type,String workID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        iTeacherDao.updateType(type, workID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void updateTel(String tel,String workID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        iTeacherDao.updateTel(tel, workID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    /**
     * 根据工号获得教师姓名
     * @param workID
     * @return
     */
    public static String getName(String workID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        Teacher teacher = iTeacherDao.find(workID);
        MySqlUtils.close();
        return teacher.getName();
    }

    /**
     * 获得学生作业
     */
    public static List<UserWork> getUserWorks(String work){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserWorkDao iUserWorkDao = MySqlUtils.getIUserWorkDao();
        List<UserWork> l = iUserWorkDao.findByWork(work);
        MySqlUtils.close();
        return l;
    }
}
