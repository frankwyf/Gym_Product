//package com.gymmaster.controller;
//
//import com.gymmaster.GymMasterApplication;
//import com.gymmaster.entity.Reservation;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
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
//import java.sql.Date;
//import java.util.Calendar;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class ReservationControllerTest {
//    private static final String BASE_URL = "http://localhost:8080/reservation/";
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//
//    @Test
//    @Transactional
//    @Rollback
//    void edit() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//
//        Reservation reservation = new Reservation();
//        reservation.setFacility(1);
//        reservation.setRuid(1);
//
//        Calendar c = Calendar.getInstance();
//        c.set(2022,Calendar.FEBRUARY,11);
//        java.util.Date date = c.getTime();
//        Date d = new Date(date.getTime());
//
//        reservation.setRdate(d);
//        reservation.setVenue(1);
//        reservation.setRid(1);
//        reservation.setPeriod("2,3");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(reservation);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .put(BASE_URL+"update")
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
//        Calendar c = Calendar.getInstance();
//        c.set(2022,Calendar.FEBRUARY,11);
//        java.util.Date date = c.getTime();
//        Date d = new Date(date.getTime());
//
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL+"/page")
//                        .param("page", String.valueOf(3))
//                        .param("pageSize", String.valueOf(1))
//                        .param("id",String.valueOf(0))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String ret1 = mockMvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL+"/page")
//                        .param("page", String.valueOf(3))
//                        .param("pageSize", String.valueOf(1))
//                        .param("id",String.valueOf(0))
//
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret1=======" + ret1);
//
////        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
////        String ret2 = mockMvc.perform(MockMvcRequestBuilders
////                        .get(BASE_URL+"/page")
////                        .param("page", String.valueOf(3))
////                        .param("pageSize", String.valueOf(1)).param("name","jin").param("date", String.valueOf(d))
////                        .param("id",String.valueOf(0))
////
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andDo(MockMvcResultHandlers.print())
////                .andReturn().getResponse().getContentAsString();
////        System.out.println("ret1=======" + ret2);
//
//
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void add() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Reservation reservation = new Reservation();
//        reservation.setPeriod("3,5");
//
//        Calendar c = Calendar.getInstance();
//        c.set(2022,Calendar.FEBRUARY,11);
//        java.util.Date date = c.getTime();
//        Date d = new Date(date.getTime());
//        reservation.setRdate(d);
//        reservation.setAmount(2);
//        reservation.setRuid(3);
//        reservation.setFacility(4);
//        reservation.setVenue(1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(reservation);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL+"add")
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
//    void update() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//
//        Reservation reservation = new Reservation();
//        reservation.setStatus("paid");
//        reservation.setRid(1);
//        reservation.setVenue(1);
//        reservation.setFacility(1);
//        reservation.setRuid(1);
//        reservation.setAmount(4);
//        reservation.setPeriod("1,2,3");
//        Calendar c = Calendar.getInstance();
//        c.set(2022,Calendar.FEBRUARY,12);
//        java.util.Date date = c.getTime();
//        Date d = new Date(date.getTime());
//        reservation.setRdate(d);
//        reservation.setPayment("card");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(reservation);
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .put(BASE_URL+"/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonStr))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//}
