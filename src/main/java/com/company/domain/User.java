package com.company.domain;

import java.io.Serializable;

/**
 * 用户信息封装类
 */
public class User implements Serializable {
    private Integer id;//主键
    private String name;//学生姓名
    private String studentID;//学号
    private String password;//密码
    private String type;//类型
    private Integer level;//等级
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studentID='" + studentID + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                '}';
    }
}