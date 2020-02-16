package com.company.domain;

import java.io.Serializable;

/**
 * 教师信息封装类
 */
public class Teacher implements Serializable {
    private Integer id;//主键
    private String name;//姓名
    private String tel;//联系方式
    private String type;//主教科目
    private String workID;//工号
    private String password;//密码

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWorkID() {
        return workID;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", type='" + type + '\'' +
                ", workID='" + workID + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}