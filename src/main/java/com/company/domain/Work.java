package com.company.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 考核信息封装类
 */
public class Work implements Serializable {
    private Integer id;//主键
    private String name;//考核名称
    private String work;//标识码
    private String workID;//发布人工号
    private Date start;//发布日期
    private Date end;//截止日期
    private Integer level;//等级
    private String type;//类别

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getWork() {
        return work;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Integer getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public static String getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(date);
    }

    public static Date toDate(String str){
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try{
            date = sdf.parse(str);
            return date;
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", work='" + work + '\'' +
                ", workID='" + workID + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", level=" + level +
                ", type='" + type + '\'' +
                '}';
    }
}