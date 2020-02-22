package com.company.service;

import com.company.dao.*;
import com.company.domain.Teacher;
import com.company.domain.User;
import com.company.domain.UserWork;
import com.company.domain.Work;
import java.util.List;

public class ManagerService {
    public static void insert(Teacher t){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        iTeacherDao.insert(t);
        MySqlUtils.commit();
        MySqlUtils.close();
    }
    public static void insert(User u){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.insert(u);
        MySqlUtils.commit();
        MySqlUtils.close();
    }
    public static List<Work> deleteTeacher(String workID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        IWorkDao iWorkDao = MySqlUtils.getIWorkDao();
        List<Work> list = iWorkDao.find(workID);
        iWorkDao.deleteAll(workID);
        MySqlUtils.commit();
        iTeacherDao.delete(workID);
        MySqlUtils.commit();
        MySqlUtils.close();
        return list;
    }
    public static List<UserWork> deleteUser(String studentID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        IUserWorkDao iUserWorkDao = MySqlUtils.getIUserWorkDao();
        List<UserWork> list = iUserWorkDao.findByStudentID(studentID);
        iUserWorkDao.deleteAll(studentID);
        MySqlUtils.commit();
        iUserDao.deleteByStudentID(studentID);
        MySqlUtils.commit();
        MySqlUtils.close();
        return list;
    }

    public static void updateTeacher(String workID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        iTeacherDao.updatePassword(workID,workID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }
    public static void updateStudent(String studentID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        iUserDao.updatePassword(studentID,studentID);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static List<Teacher> findAllTeachers(){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        ITeacherDao iTeacherDao = MySqlUtils.getITeacherDao();
        List<Teacher> teachers = iTeacherDao.findAll();
        MySqlUtils.close();
        return teachers;
    }
    public static List<User> findAllUsers(){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IUserDao iUserDao = MySqlUtils.getIUserDao();
        List<User> users = iUserDao.findAll();
        MySqlUtils.close();
        return users;
    }
}