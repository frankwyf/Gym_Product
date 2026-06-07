//package com.gymmaster.controller;
//
//import com.gymmaster.GymMasterApplication;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.nio.charset.StandardCharsets;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class FileControllerTest {
//    private MockMvc mockMvc;
//    private static final String BASE_URL= "http://localhost:8080/file";
//    @Autowired
//    private WebApplicationContext wac;
//    @Test
//    @Transactional
//    @Rollback
//    void upload() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String path = "D:\\curGymMaster\\gymmaster (2)\\gymMaster\\src\\main\\resources\\static\\Environment\\darkBack.jpg";
//        File file = new File(path);
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "file", //文件名
//                file.getName(), //originalName 相当于上传文件在客户机上的文件名
//                MediaType.IMAGE_PNG_VALUE, //文件类型
//                new FileInputStream(file) //文件流
//        );
//        ResultActions resultActions = this.mockMvc.perform(multipart("/file/upload/posts")
//                .file(mockMultipartFile));
//        resultActions.andExpect(status().isOk())
//                .andReturn().getResponse()
//                .setCharacterEncoding(StandardCharsets.UTF_8.name());
//        resultActions.andDo(print());
//
//    }
////
////    @Test
////    @Transactional
////    @Rollback
////    void download() throws Exception{
////        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////        String name = "/12d8e9b6-b0d6-45b4-819f-220d7c217ffe.png";
////        String ret = mockMvc.perform(MockMvcRequestBuilders
////                        .get(BASE_URL+"/download")
////                        .param("name", name)
////
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn().getResponse().getContentAsString();
////        System.out.println("ret=======" + ret);
////    }
////
////    @Test
////    @Transactional
////    @Rollback
////    void downloadByHutool() throws Exception{
////        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////        String name = "12d8e9b6-b0d6-45b4-819f-220d7c217ffe.png";
////
////        String ret = mockMvc.perform(MockMvcRequestBuilders
////                        .get(BASE_URL+"/download/hutool")
////                        .param("fileName",name)
////                        .contentType(MediaType.APPLICATION_JSON)
////                )
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn().getResponse().getContentAsString();
////        System.out.println("ret=======" + ret);
////    }
//}
