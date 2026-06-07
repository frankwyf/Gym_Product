//package com.gymmaster.controller;
//
//import com.gymmaster.entity.Coach;
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
//import java.math.BigDecimal;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class CoachControllerTest {
//
//    private MockMvc mockMvc;
//    private static final String BASE_URL= "http://localhost:8080/coach";
//    @Autowired
//    private WebApplicationContext wac;
//    @Test
//    @Transactional
//    @Rollback
//    void register() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Coach coach = new Coach();
//        coach.setLastName("j");
//        coach.setUsername("jj");
//        coach.setFirstName("adddd");
//        coach.setCertifications("adfasdf");
//        coach.setPassword("111111");
//        coach.setProfile("lazyman");
//        coach.setSalary(new BigDecimal(1.0));
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(coach);
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
////        Coach coach = new Coach();
////        coach.setPassword("j");
////        coach.setUsername("jj");
////
////        ObjectMapper objectMapper = new ObjectMapper();
////        String jsonStr = objectMapper.writeValueAsString(coach);
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
//    void add() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Coach coach = new Coach();
//        coach.setLastName("j");
//        coach.setUsername("jj");
//        coach.setFirstName("adddd");
//        coach.setCertifications("adfasdf");
//        coach.setPassword("111111");
//        coach.setProfile("lazyman");
//        coach.setSalary(new BigDecimal(1.0));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(coach);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL+"/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonStr))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
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
//    @Test
//    @Transactional
//    @Rollback
//    void update() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Coach coach = new Coach();
//        coach.setLastName("j");
//        coach.setUsername("jj");
//        coach.setFirstName("adddd");
//        coach.setCertifications("adfasdf");
//        coach.setPassword("111111");
//        coach.setProfile("lazyman");
//        coach.setSalary(new BigDecimal(1.0));
//        coach.setCoaid(2);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(coach);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .put(BASE_URL+"/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonStr))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void getbyId() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//
//
//
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL+"/info")
//                        .param("coachid", String.valueOf(5))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println("ret=======" + ret);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void delet() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Coach coach = new Coach();
//
//        coach.setCoaid(2);
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(coach);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .delete(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonStr))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//}
