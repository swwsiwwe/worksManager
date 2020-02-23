package com.company.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.domain.*;
import com.company.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 用户页面交互
 */
@Controller
@RequestMapping("/static")
public class UserController {
    /**
     * 用户注册
     * @param json
     * @param request
     * @param response
     */
    @RequestMapping("/user/create")
    public void create(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = (Map<String, String>)JsonService.getJson(json);
        User user = new User();
        user.setPassword(map.get("password"));
        user.setLevel(Integer.parseInt(map.get("level")));
        user.setType(map.get("type"));
        user.setStudentID(map.get("studentID"));
        user.setName(map.get("name"));
        User user1 = UserService.findByStudentID(user.getStudentID());
        if (user1 == null) {
            UserService.insert(user);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
            /*记录登录状态*/
            LoginService.saveLogin(request, response, user, Key.USER);
        }
        else {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "注册失败，账号重复");
            JsonService.write(response, js);
        }
    }

    /**
     * 用户登录
     * @param json
     * @param req
     * @param resp
     */
    @RequestMapping("/login")
    public void login(@RequestBody String json, HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> map = (Map<String, String>) JsonService.getJson(json);
        String id = map.get("id");
        int identity = Integer.parseInt(map.get("idenity"));
        String password = map.get("Password");
        String verifyCode = map.get("verifyCod");
        String val = "";
        boolean flag;
        String v = (String) req.getSession().getAttribute("verifyCode");
        req.getSession().removeAttribute("verifyCode");
        System.out.println(v);
        System.out.println(verifyCode + "!" + identity + "!" + "id" + id + "#" + password);
        if (v == null || !v.equalsIgnoreCase(verifyCode)) {
            val = "验证码错误!";
            flag = false;
        }
        else {
            if (identity == 1) {
                User user = UserService.findByStudentID(id);
                if (user == null || !user.getPassword().equals(password)) {
                    val = "账户名或密码错误!";
                    flag = false;
                } else {
                    flag = true;
                    /*记录登录状态*/
                    LoginService.saveLogin(req, resp, user, Key.USER);
                }
            }
            else if (identity == 2) {
                Teacher t = TeacherService.findByWorkID(id);
                if (t == null || !t.getPassword().equals(password)) {
                    val = "账户名或密码错误!";
                    flag = false;
                } else {
                    flag = true;
                    /*记录登录状态*/
                    LoginService.saveLogin(req, resp, t, Key.TEACHER);
                }
            }
            else {
                if (!"123".equals(id) || !"root".equals(password)) {
                    val = "账户名或密码错误!";
                    flag = false;
                } else {
                    flag = true;
                    Manager m = new Manager();
                    m.setPassword("root");
                    m.setId("123");
                    LoginService.saveLogin(req, resp, m, "manager");
                }
            }
        }
        JSONObject js = JsonService.createJson(flag);
        if (flag) {
            JsonService.putJson(js, "login", Integer.toString(identity));
            JsonService.putJson(js,"identity",Integer.toString(identity));
        } else {
            JsonService.putJson(js, "error", val);
        }
        JsonService.write(resp, js);
    }

    /**
     * 教师学生管理员用户退出
     * @param request
     * @param response
     */
    @RequestMapping("/user/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        LoginService.deleteLogin(request);
        JSONObject js = JsonService.createJson(true);
        JsonService.write(response, js);
    }

    /**
     * 返回验证码
     * @param request
     * @param response
     */
    @RequestMapping("/verifyCode")
    private void setVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            BufferedImage verifyImg = new BufferedImage(200, 69, BufferedImage.TYPE_INT_RGB);
            String randomText = VerifyCode.drawRandomText(200, 69, verifyImg);
            request.getSession().setAttribute("verifyCode", randomText);
            response.setContentType("image/png");
            OutputStream os = response.getOutputStream();
            ImageIO.write(verifyImg, "png", os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户个人信息
     * @param request
     * @param response
     */
    @RequestMapping("/user/findUser")
    public void findByStudentID(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) LoginService.getLogin(request, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            JSONObject js = JsonService.createJson(true);
            JsonService.writeUserJson(js,user);
            JsonService.write(response, js);
            LoginService.saveLogin(request, response, user, Key.USER);
        }
    }

    /**
     * 修改个人信息
     * @param s
     * @param request
     * @param response
     */
    @RequestMapping("/user/update")
    public void updateUser(@RequestBody String s,HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = (Map<String, String>) JsonService.getJson(s);
        String level = map.get("level");
        String name = map.get("name");
        String studentID = map.get("studentID");
        String type = map.get("type");
        User user1 = (User) LoginService.getLogin(request, Key.USER);
        if (user1 == null || user1.getStudentID() == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else if (!user1.getStudentID().equals(studentID)) {
            LoginService.saveLogin(request, response, user1, Key.USER);
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "学号不允许修改");
            JsonService.write(response, js);
        } else {
            String tp = user1.getStudentID();
            UserService.updateLevel(Integer.parseInt(level), tp);
            UserService.updateName(name, tp);
            UserService.updateType(type, tp);
            User new_user = UserService.findByStudentID(tp);
            LoginService.saveLogin(request, response, new_user, Key.USER);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 学生修改密码
     * @param s
     * @param request
     * @param response
     */
    @RequestMapping("/user/updatePassword")
    public void updateUserPassword(@RequestBody String s, HttpServletRequest request, HttpServletResponse response) {
        User user = (User) LoginService.getLogin(request, Key.USER);
        Map<String,String> map = (Map<String, String>) JsonService.getJson(s);
        String old_password = map.get("old_password");
        String new_password1 = map.get("new_password1");
        String new_password2 = map.get("new_password2");
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        }else if(new_password1==null||!new_password1.equals(new_password2)){
            LoginService.saveLogin(request, response, user, Key.USER);
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "确认密码和新密码必须保持一致");
            JsonService.write(response, js);
        }else if(!user.getPassword().equals(old_password)){
            LoginService.saveLogin(request, response, user, Key.USER);
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "密码错误");
            JsonService.write(response, js);
        }
        else if(!old_password.equals(new_password1)){
            UserService.updatePassword(new_password1, user.getStudentID());
            user.setPassword(new_password1);
            LoginService.saveLogin(request, response, user, Key.USER);
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 查询可提交作业
     * @param request
     * @param response
     */
    @RequestMapping("/user/getWorks")
    public void getWorks(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) LoginService.getLogin(request, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, user, Key.USER);
            String type = user.getType();
            int level = user.getLevel();
            JSONObject js = JsonService.createJson(true);
            System.out.println(type+" "+level);
            JsonService.writeArrayWorkJson(js, UserService.getWorks(type, level));
            JsonService.write(response, js);
        }
    }

    /**
     * 显示考核界面
     * @param request
     * @param response
     * @param json
     */
    @RequestMapping("/user/getWork")
    public void getWork(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {
        Map<String,String> map = (Map<String, String>) JsonService.getJson(json);
        String work = map.get("code");
        System.out.println(work);
        User user = (User) LoginService.getLogin(request, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, user, Key.USER);
            JSONObject js = JsonService.createJson(true);
            JsonService.writeWorkJson(js, UserService.getWork(work));
            JsonService.write(response, js);
        }
    }

    /**
     * 学生查询已提交作业
     * @param request
     * @param response
     */
    @RequestMapping("/user/getMyWorks")
    public void getMyWorks(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) LoginService.getLogin(request, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, user, Key.USER);
            JSONObject js = JsonService.createJson(true);
            JsonService.writeArrayUserWorkJson(js, UserService.getMyWorks(user.getStudentID()));
            System.out.println(js.toJSONString());
            JsonService.write(response, js);
        }
    }

    /**
     * 在线上传作业
     * @param sfile
     * @param req
     * @param resp
     * @throws Exception
     */
    @RequestMapping("/user/onLineUpload")
    public void upload(@RequestParam("sfile") MultipartFile sfile, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String work = req.getParameter("code");
        System.out.println(work+sfile.toString());
        User user = (User) LoginService.getLogin(req, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(resp, js);
        } else {
            LoginService.saveLogin(req, resp, user, Key.USER);
            if(sfile==null){
                JSONObject js = JsonService.createJson(false);
                JsonService.putJson(js, "error", "未检测到文件，请上传作业文件(.zip)");
                JsonService.write(resp, js);
            }else {
                /*获得上传文件名*/
                String filename = sfile.getOriginalFilename();
                /*检测后缀名*/
                String str = filename.substring(filename.lastIndexOf("."));
                if (".zip".equals(str)) {
                    filename = work + user.getStudentID() + ".zip";
                    /*文件上传*/
                    FileService.upload(req, sfile, "/uploads/", filename);
                    UserWork userWork = new UserWork();
                    userWork.setDate(new Date());
                    userWork.setStudentID(user.getStudentID());
                    userWork.setStatus(Status.JUDGING);
                    userWork.setWork(work);
                    UserService.submit(userWork);
                    JSONObject js = JsonService.createJson(true);
                    JsonService.write(resp, js);
                } else {
                    JSONObject js = JsonService.createJson(false);
                    JsonService.putJson(js, "error", "请上传“.zip”压缩文件");
                    JsonService.write(resp, js);
                }
            }
        }
    }

    /**
     * 离线上传 利用url上传
     * @param req
     * @param resp
     * @param upload
     * @param studentID
     * @throws Exception
     */
    @RequestMapping("/user/offLineUpload")
    public void upload(HttpServletRequest req, HttpServletResponse resp,@RequestParam("sfile") MultipartFile upload,@RequestParam("studentID") String studentID,@RequestParam("name") String name,@RequestParam("code") String work) throws Exception {
        User user = UserService.findByStudentID(studentID);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "账号错误");
            JsonService.write(resp, js);
        }
        else if (!user.getName().equals(name)) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "姓名错误");
            JsonService.write(resp, js);
        }
        else {
            if (upload == null) {
                JSONObject js = JsonService.createJson(false);
                JsonService.putJson(js, "error", "未检测到文件，请上传作业文件(.zip)");
                JsonService.write(resp, js);
            } else {
                /*获得上传文件名*/
                String filename = upload.getOriginalFilename();
                /*检测后缀名*/
                String str = filename.substring(filename.lastIndexOf("."));
                if (".zip".equals(str)) {
                    filename = work + user.getStudentID() + ".zip";
                    FileService.upload(req, upload, "/uploads/", filename);
                    UserWork userWork = new UserWork();
                    userWork.setDate(new Date());
                    userWork.setStudentID(user.getStudentID());
                    userWork.setStatus(Status.JUDGING);
                    userWork.setWork(work);
                    UserService.submit(userWork);
                    JSONObject js = JsonService.createJson(true);
                    JsonService.write(resp, js);
                } else {
                    JSONObject js = JsonService.createJson(false);
                    JsonService.putJson(js, "error", "请上传“.zip”压缩文件");
                    JsonService.write(resp, js);
                }
            }
        }
    }

    /**
     * 删除无效作业
     * @param request
     * @param response
     */
    @RequestMapping("/user/deleteInvalidWorks")
    public void deleteInvalidWorks(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) LoginService.getLogin(request, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, user, Key.USER);
            List<UserWork> list = UserWorkService.deleteInvalidWork(user.getStudentID());
            if(list!=null){
                for(UserWork u:list){
                    FileService.workDelete(request, "/uploads/", u.getWork()+user.getStudentID()+".zip");
                }
            }
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     * 删除/批量删除
     * @param request
     * @param response
     * @param jsons
     */
    @RequestMapping("/user/deleteUserWorks")
    public void deleteUserWorks(HttpServletRequest request, HttpServletResponse response,@RequestBody String jsons) {
        Map<String,List<Map<String,String>>> map = (Map<String, List<Map<String, String>>>) JsonService.getJson(jsons);
        List<Map<String,String>> list = map.get("work");
        User user = (User) LoginService.getLogin(request, Key.USER);
        if (user == null) {
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js, "error", "timeOut");
            JsonService.write(response, js);
        } else {
            LoginService.saveLogin(request, response, user, Key.USER);
            List<String> p=new ArrayList<>();
            for(Map<String,String> work:list){
                p.add(work.get("work"));
            }
            System.out.println(p);
            UserWorkService.deleteWorks(user.getStudentID(),p);
            for(String work:p){
                FileService.workDelete(request, "/uploads/", work+user.getStudentID()+".zip");
            }
            JSONObject js = JsonService.createJson(true);
            JsonService.write(response, js);
        }
    }

    /**
     *学生下载提交作业
     * @param request
     * @param
     * @param works
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/user/download")
    public  ResponseEntity<byte[]> UserDownload(HttpServletRequest request,@RequestBody String works) throws IOException {
        User user = (User) LoginService.getLogin(request, Key.USER);
        byte[] body = null;
        HttpHeaders headers = null;
        if (user != null) {
            String path = request.getServletContext().getRealPath("/uploads/");
            System.out.println(works);
            Map<String, Object> map = (Map<String, Object>) JsonService.getJson(works);
            List<Map<String, String>> list = (List<Map<String, String>>) map.get("work");
            if (list != null && !list.isEmpty()) {
                for (Map<String, String> work : list) {
                    File file = new File(path + work.get("work") + user.getStudentID() + ".zip");
                    InputStream is = new FileInputStream(file);
                    body = new byte[is.available()];
                    is.read(body);
                    headers = new HttpHeaders();
                    headers.add("Content-Disposition", "attachment;filename=" + file.getName());
                }
            }
        }
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
        return entity;
    }

    /**
     * 学生批量下载文件
     * @param request
     * @param works
     * @return 下载文件流
     * @throws IOException
     */
    @RequestMapping("/user/downloadAll")
    public ResponseEntity<byte[]> downloadAll(HttpServletRequest request,@RequestBody String works) throws IOException {
        User user = (User) LoginService.getLogin(request, Key.USER);
        File file=null;
        HttpHeaders headers=null;
        if(user!=null){
            Map<String, Object> map = (Map<String, Object>) JsonService.getJson(works);
            System.out.println(map.get("work"));
            List<Map<String, String>> list = (List<Map<String, String>>) map.get("work");
            String resourcesName = "kao_he.zip";
            String path = request.getServletContext().getRealPath("/uploads/");
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path+resourcesName));
            InputStream input = null;
            if (list != null && !list.isEmpty()) {
                for (Map<String, String> work : list) {
                    String name = path + work.get("work") + user.getStudentID() + ".zip";
                    input = new FileInputStream(new File(name));
                    zipOut.putNextEntry(new ZipEntry(work.get("work") + user.getStudentID() + ".zip"));
                    int temp = 0;
                    while ((temp = input.read()) != -1) {
                        zipOut.write(temp);
                    }
                }
            }
            input.close();
            zipOut.close();
            file = new File(path+resourcesName);
            headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + file.getName());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.OK);
    }

    /**
     * 考核内容展示
     * @param request
     * @param response
     * @param json
     */
    @RequestMapping("/user/getContent")
    public void getContent(HttpServletRequest request,HttpServletResponse response,@RequestBody String json) {
        Map<String,String> map =(Map<String, String>)JsonService.getJson(json);
        String work =map.get("code");
        String path = request.getServletContext().getRealPath("/works/");
        File file = new File(path + work +  ".txt");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while((s = br.readLine())!=null){
                sb.append(System.lineSeparator()+s);
            }
            br.close();
            JSONObject js = JsonService.createJson(true);
            JsonService.putJson(js,"content",sb.toString());
            JsonService.write(response,js);
        }catch (IOException ie){
            JSONObject js = JsonService.createJson(false);
            JsonService.putJson(js,"content","内容找不到了");
            JsonService.write(response,js);
        }
    }
}