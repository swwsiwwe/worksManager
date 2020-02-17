package com.company.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.domain.*;
import com.company.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/01")
public class TeacherController {
    /**
     * ok
     * 查询考核信息（教师）考核管理
     * @param request
     * @param response
     */
    @RequestMapping("/teacher/selectWorks")
    public void selectWorks(HttpServletRequest request, HttpServletResponse response) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, t, Key.TEACHER);
            JSONObject js = JsonService.createJson(true);
            JsonService.writeArrayWorkJson(js, WorkService.findWorkById(t.getWorkID()));
            JsonService.write(response, js);
        }
    }

    /**
     * okk
     * 发布考核
     */
    @RequestMapping("/teacher/newWork")
    public void newWork(String name, String type, int level, String end,@RequestParam("sfile") MultipartFile upload, HttpServletRequest request, HttpServletResponse response) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        Work work = new Work();
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, t, Key.TEACHER);
            work.setType(type);
            work.setLevel(level);
            work.setName(name);
            work.setEnd(Work.toDate(end));
            work.setWorkID(t.getWorkID());
            work.setStart(new Date());
            if(upload==null){
                JSONObject js = JsonService.createJson(false);
                JsonService.putJson(js, "error", "未检测到文件，请上传考核文件(.txt)");
                JsonService.write(response, js);
            }else{
                /*获取文件名*/
               String filename = upload.getOriginalFilename();
                String str = filename.substring(filename.lastIndexOf("."));
                /*检测后缀*/
                if (".txt".equals(str)) {
                    int id = WorkService.insertWork(work);
                    filename = type + "" + level + "" + id + ".txt";
                    try {
                        UploadService.upload(request, upload, "/works/", filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONObject js = JsonService.createJson(true);
                    JsonService.putJson(js,"work",type + "" + level + "" + id);
                    JsonService.write(response, js);
                } else {
                    JSONObject js = JsonService.createJson(false);
                    JsonService.putJson(js, "error", "请上传“.txt”压缩文件");
                    JsonService.write(response, js);
                }
            }

        }
    }
    /**/

    /**
     * ok
     * @param request
     * @param response
     * @param teacher
     */
    @RequestMapping("/teacher/findTeacher")
    public void findTeacher(HttpServletRequest request, HttpServletResponse response, Teacher teacher) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            JSONObject js = JsonService.createJson(true);
            JsonService.writeTeacherJson(js, t);
            JsonService.write(response, js);
            LoginService.saveLogin(request, response, t, Key.TEACHER);
        }
    }

    /**
     * 修改教师信息
     *ok
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/teacher/update")
    public void updateTeacher(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        Map<String,String> map = (Map<String, String>) JsonService.getJson(json);
        String name = map.get("name");
        String workID = map.get("workID");
        String tel = map.get("tel");
        String type =map.get("type");
        if (t == null || t.getWorkID() == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else if (!t.getWorkID().equals(workID)) {
            LoginService.saveLogin(request, response, t, Key.USER);
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "工号不允许修改");
            JsonService.write(response, js);
        } else {
            String tp = t.getWorkID();
            TeacherService.updateName(name, tp);
            TeacherService.updateTel(tel, tp);
            TeacherService.updateType(type, tp);
            Teacher teacher1 = TeacherService.findByWorkID(tp);
            LoginService.saveLogin(request, response, teacher1, Key.TEACHER);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * ok
     * 修改密码
     * @param request
     * @param response
     */

    @RequestMapping("/teacher/updatePassword")
    public void updateUserPassword(@RequestBody String s, HttpServletRequest request, HttpServletResponse response) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        System.out.println(t);
        Map<String,String> map = (Map<String, String>) JsonService.getJson(s);
        System.out.println(map);
        String old_password = map.get("old_password");
        String new_password1 = map.get("new_password1");
        String new_password2 = map.get("new_password2");
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        }else if(new_password1==null||!new_password1.equals(new_password2)){
            LoginService.saveLogin(request, response, t, Key.USER);
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "确认密码和新密码必须保持一致");
            JsonService.write(response, js);
        }else if(!t.getPassword().equals(old_password)){
            LoginService.saveLogin(request, response, t, Key.USER);
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "密码错误");
            JsonService.write(response, js);
        }
        else if(!old_password.equals(new_password1)){
            TeacherService.updatePassword(new_password1, t.getWorkID());
            t.setPassword(new_password1);
            LoginService.saveLogin(request, response, t, Key.USER);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * ok
     * @param request
     * @param response
     * @param work
     * @param name
     * @param end
     * @param upload
     */
    @RequestMapping("/teacher/updateWork")
    public void updateWork(HttpServletRequest request, HttpServletResponse response,@RequestParam("code") String work,String name, String end,@RequestParam("sfile") MultipartFile upload) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, t, Key.TEACHER);
            WorkService.update(work,name,end);
            if(upload==null||upload.isEmpty()){
                JSONObject js = JsonService.createJson(false);
                JsonService.putJson(js, "error", "未检测到文件，请上传考核文件(.txt)");
                JsonService.write(response, js);
            }else{
                /*获取文件名*/
                String filename = upload.getOriginalFilename();
                String str = filename.substring(filename.lastIndexOf("."));
                /*检测后缀*/
                if (".txt".equals(str)) {
                    filename = work + ".txt";
                    try {
                        UploadService.upload(request, upload, "/works/", filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONObject js = JsonService.createJson(true);
                    JsonService.write(response, js);
                } else {
                    JSONObject js = JsonService.createJson(false);
                    JsonService.putJson(js, "error", "请上传“.txt”压缩文件");
                    JsonService.write(response, js);
                }
            }
        }
    }

    /**
     * ok
     * 查看提交
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/teacher/judgeWork")
    public void judgeWork(HttpServletRequest request, HttpServletResponse response,@RequestBody String json) {
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        Map<String,String> map = (Map<String, String>) JsonService.getJson(json);
        System.out.println(map);
        String work = map.get("code");
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, t, Key.TEACHER);
            List<UserWork> list = TeacherService.getUserWorks(work);
            JSONObject js = JsonService.createJson(true);
            JsonService.writeTeacherWorkJson(js, list);
            JsonService.write(response, js);
        }
    }

    /**
     * ok
     * 作业批改
     * @param request
     * @param response
     */
    @RequestMapping("/teacher/judge")
    public void judge(HttpServletRequest request, HttpServletResponse response,@RequestBody String json) {
        System.out.println(json);
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        Map<String,String> map = (Map<String, String>) JsonService.getJson(json);
        String status = map.get("status");
        String studentID =map.get("studentID");
        String work = map.get("code");
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, t, Key.TEACHER);
            UserWorkService.updateStatus(status, studentID,work);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 删除考核
     * @param request
     * @param response
     */
    @RequestMapping("/teacher/deleteWork")
    public void deleteWork(@RequestBody String jsons,HttpServletRequest request, HttpServletResponse response) {
        System.out.println(jsons);
        Map<String,List<Map<String,String>>> map = (Map<String, List<Map<String, String>>>) JsonService.getJson(jsons);
        System.out.println(map);
        List<Map<String,String>> list = map.get("work");
        System.out.println(list);
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, t, Key.TEACHER);
            for(Map<String,String> work:list) {
                if (work != null) {
                    WorkService.delete(work.get("work"));
                    UserWorkService.deleteWork("已删除", work.get("work"));
                    UploadService.workDelete(request, "/works/", work.get("work") + ".txt");
                }
            }
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    @RequestMapping("/teacher/download")
    public ResponseEntity<byte[]> WorkDownload(HttpServletRequest request, String work) throws IOException {
        String path = request.getServletContext().getRealPath("/works/");
        File file = new File(path + work + ".txt");
        byte[] body = null;
        InputStream is = new FileInputStream(file);
        body = new byte[is.available()];
        is.read(body);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + file.getName());
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        return entity;
    }

    /**
     * 考核详情
     * ok
     * @param request
     * @param response
     * @param json
     * @throws IOException
     */
    @RequestMapping("/teacher/getWork")
    public void getWork(HttpServletRequest request, HttpServletResponse response,@RequestBody String json) throws IOException {
        Map<String,String> map = (Map<String, String>) JsonService.getJson(json);
        String work = map.get("code");
        Teacher t = (Teacher) LoginService.getLogin(request, Key.TEACHER);
        if (t == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
        } else {
            Work w = WorkService.findWorkByCode(work);
            JSONObject js = JsonService.createJson(true);
            JsonService.writeWorkJson(js, w);
            JsonService.write(response, js);
        }
    }
}