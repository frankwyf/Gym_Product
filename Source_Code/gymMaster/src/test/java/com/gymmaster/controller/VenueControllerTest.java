//package com.gymmaster.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gymmaster.GymMasterApplication;
//import com.gymmaster.entity.Venue;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.junit.jupiter.api.Assertions.*;
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class VenueControllerTest {
//    private static final String BASE_URL = "http://localhost:8080/venue/";
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
////    @Test
////    void getById() throws Exception {
////        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////        Venue venue = new Venue();
////        venue.setVid(1);
////        ObjectMapper objectMapper = new ObjectMapper();
////        String jsonStr = objectMapper.writeValueAsString(venue);
////        String ret = mockMvc.perform(MockMvcRequestBuilders
////                        .get(BASE_URL+"getById")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(jsonStr))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn().getResponse().getContentAsString();
////        System.out.println("ret=======" + ret);
////    }
//
//    @Test
//    void getAvailableVenues() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL+"/getAvailableVenues")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//}
