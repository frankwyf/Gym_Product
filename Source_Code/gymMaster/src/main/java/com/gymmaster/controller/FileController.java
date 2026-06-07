package com.gymmaster.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.service.CustomerService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    RedisCache redisCache;
    @Autowired
    CustomerService customerService;
    @PostMapping("/upload/customer")
    public BackMsg<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String orgin = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String suffix = orgin.substring(orgin.lastIndexOf("."));
        fileName = fileName+suffix;
        String name = "";
        name = basePath + "customerpro/";
        File dir = new File(name);
        if(!dir.exists()){
            dir.mkdirs();
        }
        Path path = Paths.get(name + fileName);
        Files.write(path, file.getBytes());
        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;

        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        user.getCustomer().setProfile(fileName);
        LambdaQueryWrapper<Customer> customerLambdaQueryWrapper =new LambdaQueryWrapper<>();

        customerLambdaQueryWrapper.eq(Customer::getUid,user.getCustomer().getUid());
        redisCache.setCacheObject(redisKey,user);
        customerService.update(user.getCustomer(),customerLambdaQueryWrapper);
        return BackMsg.success(fileName);
    }
@PostMapping("/upload/posts")
    public BackMsg<String> uploadposts(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String orgin = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String suffix = orgin.substring(orgin.lastIndexOf("."));
        fileName = fileName+suffix;
        String name = "";
        name = basePath + "posts/";
        File dir = new File(name);
        if(!dir.exists()){
            dir.mkdirs();
        }
        Path path = Paths.get(name + fileName);
        Files.write(path, file.getBytes());
        return BackMsg.success(fileName);
    }



    // for download files and write to database
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //get the required file through file input stream
            String[] f = name.split("\\.");
            FileInputStream fileInputStream;
            ResourceLoader resourceLoader = null;
            //get the file output stream
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = null;
            if(f[1].equals("png")) {
                response.setContentType("image/png");
                fileInputStream = new FileInputStream(new File(basePath + "/static/customerpro/" +name));
            }
            else  {
                response.setContentType("image/jpg");
                inputStream = resourceLoader.getResource(basePath + "/static/customerpro/"+name).getInputStream();
            }

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
