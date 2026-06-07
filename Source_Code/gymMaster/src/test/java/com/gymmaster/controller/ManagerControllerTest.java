//package com.gymmaster.controller;
//
//import com.gymmaster.entity.Manager;
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
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class ManagerControllerTest {
//    private MockMvc mockMvc;
//    private static final String BASE_URL= "http://localhost:8080/manager";
//    @Autowired
//    private WebApplicationContext wac;
//    @Test
//    @Transactional
//    @Rollback
//    void register() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Manager employee = new Manager();
//        employee.setPassword("test1");
//        employee.setUsername("test3");
//        employee.setPhone("test1");
//        employee.setProfile("test1");
//        employee.setEmail("test2");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(employee);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL+"/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonStr))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
////    @Test
////    @Transactional
////    @Rollback
////    void login() throws Exception {
////        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////        Manager manager = new Manager();
////        manager.setPassword("test1");
////        manager.setUsername("test3");
////
////        ObjectMapper objectMapper = new ObjectMapper();
////        String jsonStr = objectMapper.writeValueAsString(manager);
////        String ret = mockMvc.perform(MockMvcRequestBuilders
////                        .get(BASE_URL+"/login")
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
//
//}
