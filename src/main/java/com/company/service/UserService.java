package com.company.service;

import com.company.dao.IUserDao;
import com.company.dao.IUserWorkDao;
import com.company.dao.IWorkDao;
import com.company.dao.MySqlUtils;
import com.company.domain.User;
import com.company.domain.UserWork;
import com.company.domain.Work;
import java.util.Date;
import java.util.List;

public class UserService {

    public static User findByStudentID(String studentID){
        MySqlUtils MySqlUtils = new MySqlUtils();

        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        User u = iUserDao.findByStudentID(studentID);
        MySqlUtils.close();
        return u;
    }

    public static void updateName(String name,String studentID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.updateName(name, studentID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void updatePassword(String password,String studentID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.updatePassword(password, studentID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void updateType(String type,String studentID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.updateType(type, studentID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void updateLevel(int level,String studentID) {
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.updateLevel(level, studentID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void insert(User user){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.insert(user);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void submit(UserWork userWork){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserWorkDao iUserWorkDao = MySqlUtils.getIUserWorkDao();
        UserWork w = iUserWorkDao.findWork(userWork.getWork(),userWork.getStudentID());
        if(w==null){
            iUserWorkDao.insert(userWork);
            MySqlUtils.commit();
            MySqlUtils.close();
        }
        else{
            iUserWorkDao.updateDate(new Date(),w.getStudentID(),w.getWork());
            MySqlUtils.commit();
            MySqlUtils.close();
        }
    }

    public static List<Work> getWorks(String type,int level){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao=MySqlUtils.getIWorkDao();
        List<Work> l = iWorkDao.findByUser(type,level);
        MySqlUtils.close();
        return l;
    }
    public static Work getWork(String work){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao=MySqlUtils.getIWorkDao();
        Work w = iWorkDao.findByWork(work);
        MySqlUtils.close();
        return w;
    }

    public static List<UserWork> getMyWorks(String studentID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserWorkDao iUserWorkDao=MySqlUtils.getIUserWorkDao();
        List<UserWork> l = iUserWorkDao.findByStudentID(studentID);
        MySqlUtils.close();
        return l;
    }
}