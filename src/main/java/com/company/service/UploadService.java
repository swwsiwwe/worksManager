package com.company.service;

import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class UploadService {
    public static void upload(HttpServletRequest request, MultipartFile upload, String path, String filename) throws Exception{
        /*创建文件夹*/
        path = request.getServletContext().getRealPath(path);
        File file = new File(path);
        System.out.println(path);
        if(!file.exists()){
            file.mkdirs();
        }
        /*文件上传*/
        upload.transferTo(new File(path,filename));
    }

    /**
     * 文件删除
     * @param request
     * @param realPath
     */
    public static void workDelete(HttpServletRequest request,String realPath,String filename) {
        realPath = request.getServletContext().getRealPath(realPath);
        File file = new File(realPath+filename);
        file.delete();
    }
}
