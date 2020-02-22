package com.company.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交作业信息封装类
 */
public class UserWork implements Serializable {
    private Integer id;//主键
    private String studentID;//作者学号
    private Date date;//提交日期
    private String status;//审核状态
    private String work;//标识

    public Integer getId() {
        return id;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getWork() {
        return work;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWork(String work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return "UserWork{" +
                "id=" + id +
                ", studentID='" + studentID + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", work='" + work + '\'' +
                '}';
    }
}