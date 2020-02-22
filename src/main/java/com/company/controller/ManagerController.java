package com.company.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.domain.*;
import com.company.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 管理员页面交互
 */
@Controller
@RequestMapping("/static")
public class ManagerController {
    /**
     * 创建教师用户
     * @param request
     * @param response
     * @param json
     */
    @RequestMapping("/manager/create")
    public void createTeacher(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {
        Map<String, String> map = (Map<String, String>) JsonService.getJson(json);
        String tel = map.get("phone");
        String name = map.get("name");
        String workID = map.get("code");
        String type = map.get("type");
        System.out.println(tel+" "+name+" "+workID+" "+type);
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        }
        else {
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            Teacher t = new Teacher();
            if (workID != null&&!workID.equals("")) {
                if(TeacherService.findByWorkID(workID)==null){
                    t.setWorkID(workID);
                    t.setPassword(workID);
                    t.setName(name);
                    t.setType(type);
                    t.setTel(tel);
                    ManagerService.insert(t);
                    JSONObject js = JsonService.createJson(true);
                    JsonService.write(response, js);
                }
                else{
                    JSONObject js = JsonService.createJson(false);
                    JsonService.putJson(js, "error", "工号已存在");
                    JsonService.write(response, js);
                }
            }
            else {
                JSONObject js = JsonService.createJson(false);
                JsonService.putJson(js, "error", "工号不能为空");
                JsonService.write(response, js);
            }
        }
    }

    /**
     * 批量删除学生或教师用户
     * @param request
     * @param response
     */
    @RequestMapping("/manager/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        Map<String, Object> map = (Map<String, Object>) JsonService.getJson(json);
        String key = (String) map.get("key");
        List<Map<String, String>> IDList = (List<Map<String, String>>) map.get("id");
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            if ("student".equals(key)) {
                for (Map<String, String> IDMap : IDList) {
                    List<UserWork> list = ManagerService.deleteUser(IDMap.get("id"));
                    if (list != null && !list.isEmpty()) {
                        for (UserWork userWork : list) {
                            if (userWork != null)
                                FileService.workDelete(request, "/uploads/", userWork.getWork() + userWork.getStudentID() + ".zip");
                        }
                    }
                }
            } else if ("teacher".equals(key)) {
                for (Map<String, String> IDMap : IDList) {
                    List<Work> list = ManagerService.deleteTeacher(IDMap.get("id"));
                    if (list != null && !list.isEmpty()) {
                        for (Work work : list) {
                            if (work != null) {
                                FileService.workDelete(request, "/works/", work.getWork() + ".txt");
                                /*延迟删除标志*/
                                UserWorkService.deleteWork("作业已被删除", work.getWork());
                            }
                        }
                    }
                }
            }
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 查找学生名单
     * @param request
     * @param response
     */
    @RequestMapping("/manager/selectUsers")
    public void selectUsers(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(LoginService.getLogin(request, Key.MANAGER).toString());
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            List<User> list = ManagerService.findAllUsers();
            JSONObject js = JsonService.createJson(true);
            JsonService.writeUsersJson(js, list);
            JsonService.write(response, js);
        }
    }

    /**
     * 查找教师名单
     * @param request
     * @param response
     */
    @RequestMapping("/manager/selectTeachers")
    public void selectTeachers(HttpServletRequest request, HttpServletResponse response) {
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            List<Teacher> list = ManagerService.findAllTeachers();
            JSONObject js = JsonService.createJson(true);
            JsonService.writeTeachersJson(js, list);
            JsonService.write(response, js);
        }
    }

    /**
     * 教师学生密码初始化
     * @param request
     * @param response
     */
    @RequestMapping("/manager/updatePassword")
    public void updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsons) {
        Manager m = (Manager) LoginService.getLogin(request, Key.MANAGER);
        Map<String, Object> map = (Map<String, Object>) JsonService.getJson(jsons);
        String key = (String) map.get("key");
        List<Map<String, String>> IDList = (List<Map<String, String>>) map.get("id");
        System.out.println(jsons);
        if (m == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        }
        else {
            LoginService.saveLogin(request, response, m, Key.MANAGER);
            if ("student".equals(key)) {
                for (Map<String, String> IDMap : IDList) {
                    ManagerService.updateStudent(IDMap.get("id"));
                }

            } else if ("teacher".equals(key)) {
                for (Map<String, String> IDMap : IDList) {
                    ManagerService.updateTeacher(IDMap.get("id"));
                }
            }
        }
        JSONObject js = JsonService.createJson(true);
        JsonService.write(response, js);
    }
}