package com.company.service;

import com.company.dao.IWorkDao;
import com.company.dao.MySqlUtils;
import com.company.domain.Work;
import java.util.Date;
import java.util.List;

public class WorkService {

    public static Work findWorkByCode(String work){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao= MySqlUtils.getIWorkDao();
        Work w = iWorkDao.findByWork(work);
        MySqlUtils.close();
        return w;
    }

    public static List<Work> findWorkById(String workID){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao= MySqlUtils.getIWorkDao();
        List<Work> l = iWorkDao.find(workID);
        MySqlUtils.close();
        return l;
    }

    public static int insertWork(Work work){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao= MySqlUtils.getIWorkDao();
        String s = work.getType()+work.getLevel();
        iWorkDao.insert(work);
        s=s+work.getId();
        iWorkDao.updateWork(s,work.getId());
        MySqlUtils.commit();
        MySqlUtils.close();
        return work.getId();
    }

    public static void delete(String work){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao= MySqlUtils.getIWorkDao();
        iWorkDao.delete(work);
        MySqlUtils.commit();
        MySqlUtils.close();
    }

    public static void update(String work,String name,String end){
        MySqlUtils MySqlUtils = new MySqlUtils();
        MySqlUtils.init();
        IWorkDao iWorkDao= MySqlUtils.getIWorkDao();
        iWorkDao.updateEnd(Work.toDate(end),work);
        iWorkDao.updateName(name,work);
        iWorkDao.updateStart(new Date(),work);
        MySqlUtils.commit();
        MySqlUtils.close();
    }
}