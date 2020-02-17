package com.company.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.domain.*;
import com.company.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 管理员控制类
 */
@Controller
@RequestMapping("/01")
public class ManagerController {
    @RequestMapping("/manager/create")
    public void createTeacher(HttpServletRequest request,HttpServletResponse response,String workId,String type){
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        }else{
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            Teacher t = new Teacher();
            t.setWorkID(workId);
            t.setPassword(workId);
            t.setName(workId);
            t.setType(type);
            ManagerService.insert(t);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 删除
     * @param request
     * @param response
     * @param key
     * @param id
     */
    @RequestMapping("/manager/delete")
    public void delete(HttpServletRequest request,HttpServletResponse response,String key,String id) {
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            if ("student".equals(key)) {
                List<UserWork> list = ManagerService.deleteUser(id);
                if (list != null && !list.isEmpty()) {
                    for (UserWork userWork : list) {
                        if (userWork != null)
                            UploadService.workDelete(request, "/uploads/", userWork.getWork() + userWork.getStudentID() + ".zip");
                    }
                }
            } else if ("teacher".equals(key)) {
                List<Work> list = ManagerService.deleteTeacher(id);
                if (list != null && !list.isEmpty()) {
                    for (Work work : list) {
                        if (work != null) {
                            UploadService.workDelete(request, "/works/", work.getWork() + ".txt");
                            /*延迟删除标志*/
                            UserWorkService.deleteWork("作业已被删除", work.getWork());
                        }
                    }
                }
            }
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 用户名单
     * @param request
     * @param response
     */
    @RequestMapping("/manager/selectUsers")
    public void selectUsers(HttpServletRequest request,HttpServletResponse response){
        System.out.println(LoginService.getLogin(request, Key.MANAGER).toString());
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if(m==null){
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js,"error","timeOut");
            JsonService.write(response,js);
        }else{
            LoginService.saveLogin(request,response,m,Key.MANAGER);
            List<User> list = ManagerService.findAllUsers();
            JSONObject js = JsonService.createJson(true);
            JsonService.writeUsersJson(js,list);
            JsonService.write(response,js);
        }
    }

    /**
     * 教师名单
     * @param request
     * @param response
     */
    @RequestMapping("/manager/selectTeachers")
    public void selectTeachers(HttpServletRequest request,HttpServletResponse response){
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if(m==null){
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js,"error","timeOut");
            JsonService.write(response,js);
        }else{
            LoginService.saveLogin(request,response,m,Key.MANAGER);
            List<Teacher> list = ManagerService.findAllTeachers();
            JSONObject js = JsonService.createJson(true);
            JsonService.writeTeachersJson(js,list);
            JsonService.write(response,js);
        }
    }

    /**
     * 密码初识话
     * @param request
     * @param response
     * @param key
     * @param id
     */
    @RequestMapping("/manager/updatePassword")
    public void updatePassword(HttpServletRequest request,HttpServletResponse response,String key,String id){
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if(m==null){
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js,"error","timeOut");
            JsonService.write(response,js);
        }else{
            LoginService.saveLogin(request,response,m,Key.MANAGER);
            if("student".equals(key)){
                ManagerService.updateStudent(id);
            }else if("teacher".equals(key)){
                ManagerService.updateTeacher(id);
            }
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response,js);
        }
    }
}
