package com.company.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.domain.Teacher;
import com.company.domain.User;
import com.company.domain.UserWork;
import com.company.domain.Work;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Json创建修改
 */
public class JsonService {
    public static void putJson(JSONObject jsonObject,String key,String val)  {
        jsonObject.put(key,val);
    }
    public static void write(HttpServletResponse response,JSONObject jsonObject)  {
        response.setContentType("text/html;charset=utf-8");
        try{
            response.getWriter().write(jsonObject.toJSONString());
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    public static JSONObject createJson(boolean flag){
        JSONObject js = new JSONObject();
        js.put("status",flag);
        return js;
    }

    public static void writeUserJson(JSONObject jsonObject,User user){
        if(user==null){
            jsonObject.put("name",null);
            jsonObject.put("studentID",null);
            jsonObject.put("type",null);
            jsonObject.put("level",null);
        }
        else{
            jsonObject.put("name",user.getName());
            jsonObject.put("studentID",user.getStudentID());
            jsonObject.put("type",user.getType());
            jsonObject.put("level",user.getLevel());
        }
    }

    public static void writeTeacherJson(JSONObject jsonObject,Teacher teacher){
        if(teacher==null){
            jsonObject.put("name",null);
            jsonObject.put("workID",null);
            jsonObject.put("tel",null);
            jsonObject.put("type",null);
        }
        else{
            jsonObject.put("name", teacher.getName());
            jsonObject.put("workID", teacher.getWorkID());
            jsonObject.put("tel", teacher.getTel());
            jsonObject.put("type", teacher.getType());
        }
    }

    public static void writeTeachersJson(JSONObject js,List<Teacher> list){
        JSONArray jsonArray = new JSONArray();
        if(list!=null&&!list.isEmpty()){
            for(Teacher teacher:list){
                if(teacher!=null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", teacher.getName());
                    jsonObject.put("workID", teacher.getWorkID());
                    jsonArray.add(jsonObject);
                }
            }
        }
        js.put("teacher",jsonArray);
    }

    public static void writeUsersJson(JSONObject js,List<User> list){
        JSONArray jsonArray = new JSONArray();

        if(list!=null&&!list.isEmpty()){
            for(User user:list){
                if(user!=null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", user.getName());
                    jsonObject.put("studentID", user.getStudentID());
                    jsonArray.add(jsonObject);
                }
            }
        }
        js.put("student",jsonArray);
    }

    public static void writeWorkJson(JSONObject o,Work work) {
        JSONObject jsonObject = new JSONObject();
        if (work == null) {
            jsonObject.put("name", null);
            jsonObject.put("code", null);
            jsonObject.put("teacher", null);
            jsonObject.put("start", null);
            jsonObject.put("end", null);
        } else {
            jsonObject.put("name", work.getName());
            jsonObject.put("code", work.getWork());
            jsonObject.put("teacher", TeacherService.getName(work.getWorkID()));
            jsonObject.put("start", Work.getDate(work.getStart()));
            jsonObject.put("end", Work.getDate(work.getEnd()));
        }
        o.put("work", jsonObject);
    }

    public static void writeArrayWorkJson(JSONObject o,List<Work> list){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        if(list!=null&&!list.isEmpty()){
            for(Work w:list){
                if(w!=null){
                    jsonObject= new JSONObject();
                    jsonObject.put("name",w.getName()==null?"无":w.getName());
                    jsonObject.put("code",w.getWork());
                    jsonObject.put("teacher",TeacherService.getName(w.getWorkID()));
                    jsonObject.put("grade",w.getLevel());
                    jsonObject.put("type",w.getType());
                    jsonObject.put("end",Work.getDate(w.getEnd()));
                    jsonArray.add(jsonObject);
                }
            }
        }
        o.put("work",jsonArray);
    }

    public static void writeArrayUserWorkJson(JSONObject o,List<UserWork> list){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        if(list!=null&&!list.isEmpty()){
            for(UserWork w1:list){
                Work w = WorkService.findWorkByCode(w1.getWork());
                jsonObject= new JSONObject();
                if(w!=null){
                    jsonObject.put("name",w.getName()==null?"无":w.getName());
                    jsonObject.put("code",w.getWork());
                    jsonObject.put("teacher",TeacherService.getName(w.getWorkID()));
                    jsonObject.put("grade",w.getLevel());
                    jsonObject.put("type",w.getType());
                    jsonObject.put("date",Work.getDate(w1.getDate()));
                    jsonObject.put("status",w1.getStatus());
                    jsonArray.add(jsonObject);
                }else{
                    jsonObject.put("name","无");
                    jsonObject.put("code",w1.getWork());
                    jsonObject.put("teacher","无");
                    jsonObject.put("grade",UserService.findByStudentID(w1.getStudentID()).getLevel());
                    jsonObject.put("type",UserService.findByStudentID(w1.getStudentID()).getType());
                    jsonObject.put("date",Work.getDate(w1.getDate()));
                    jsonObject.put("status",w1.getStatus());
                    jsonArray.add(jsonObject);
                }
            }
        }
        o.put("userWork",jsonArray);
    }

    public static void writeTeacherWorkJson(JSONObject o,List<UserWork> list) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        if (list!=null&&!list.isEmpty()) {
            for (UserWork w1 : list) {
                if(w1!=null){
                    jsonObject = new JSONObject();
                    String name = UserService.findByStudentID(w1.getStudentID()).getName();
                    jsonObject.put("name", name);
                    jsonObject.put("studentID", w1.getStudentID());
                    jsonObject.put("date", Work.getDate(w1.getDate()));
                    jsonObject.put("status", w1.getStatus());
                    jsonArray.add(jsonObject);
                }
            }
        }
        o.put("teacherWork", jsonArray);
    }
    public static Object getJson(String s){
        return JSONObject.parse(s);
    }
}