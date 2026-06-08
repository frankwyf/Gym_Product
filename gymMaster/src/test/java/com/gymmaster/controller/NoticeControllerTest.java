//package com.gymmaster.controller;
//
//import com.gymmaster.entity.Notice;
//import com.gymmaster.GymMasterApplication;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class NoticeControllerTest {
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//    private static final String BASE_URL= "http://localhost:8080/notice";
//
//
////    @Test
////    @Transactional
////    @Rollback
////    void add() throws Exception {
////        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////        Notice notice = new Notice();
////
////        notice.setNoticeMedia("phone");
////        notice.setContent("aaa");
////        notice.setTitle("bbb");
////
////        ObjectMapper objectMapper = new ObjectMapper();
////        String jsonStr = objectMapper.writeValueAsString(notice);
////        String ret = mockMvc.perform(MockMvcRequestBuilders
////                        .post(BASE_URL+"/add")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(jsonStr))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn().getResponse().getContentAsString();
////        System.out.println("ret=======" + ret);
////    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void delet() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Notice notice = new Notice();
//
//        notice.setNid(1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(notice);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .delete(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonStr))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//
//    @Test
//    void page() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL+"/page")
//                        .param("page", String.valueOf(3))
//                        .param("pageSize",String.valueOf(3))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//}
