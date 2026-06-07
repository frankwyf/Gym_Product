package com.gym.project.gymmaster.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.gym.project.gymmaster.common.BackMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *  The class is used to upload and download the file
 *  because files are static recourse, so we need to add the resource under static folder
 *  then we can use the url to access the file
 * @author: XJCO2913 Group2
 * @date: 2023/3/13
 * @version: 1.0
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${gym.path}")
    private String basePath;
    @PostMapping("/upload")
    public BackMsg<String> upload(MultipartFile file){
        String orgin = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String suffix = orgin.substring(orgin.lastIndexOf("."));
        fileName = fileName+suffix;
        File dir;
        if (suffix.equals(".jpeg")||suffix.equals(".jpg")||suffix.equals(".png")){
            dir = new File(basePath+"picture");
        }
        else{
            dir = new File(basePath+"vedio");
        }
        try {
            file.transferTo(new File(dir+"/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BackMsg.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //get the required file through file input stream
            String[] f = name.split(".");
            FileInputStream fileInputStream;

            //get the file output stream
            ServletOutputStream outputStream = response.getOutputStream();
            if(f[1].equals("png")) {
                response.setContentType("image/png");
                fileInputStream = new FileInputStream(new File(basePath + "/picture/"+"name"));
            }
            else if (f[1].equals("jpeg") || f[1].equals("jpg")){
                response.setContentType("image/jpg");
                fileInputStream = new FileInputStream(new File(basePath + "/picture/"+"name"));
            }
            else {
                response.setContentType("vedio/mp4");
                fileInputStream = new FileInputStream(new File(basePath + "/vedio/"+"name"));
            }
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
 //           ExcelWriter excelWriter = EasyExcel.write(out).build();

            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/download/hutool")
    @ResponseBody
    public void downloadByHutool(@RequestParam(value = "fileName") String fileName,
                                 HttpServletResponse response) {
        //防止中文乱码
        String[] group = fileName.split("\\.");
        response.setCharacterEncoding("UTF-8");
        if(group[1].equals("png") || group[1].equals("jpg") || group[1].equals("jpeg")){
            ServletUtil.write(response,new File(basePath + "picture/"+fileName));
        }
        else {
            ServletUtil.write(response, new File(basePath + "vedio/" + fileName));
        }
    }

}
