//package com.gymmaster.controller;
//
//import com.gymmaster.entity.Bill;
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
//import java.sql.Date;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {GymMasterApplication.class })//这里加启动类
//class BillControllerTest {
//    private MockMvc mockMvc;
//    private static final String BASE_URL= "http://localhost:8080/bill";
//    @Autowired
//    private WebApplicationContext wac;
//    @Test
//    void pagePeriod() throws Exception {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Calendar c = Calendar.getInstance();
//        c.set(2022,Calendar.APRIL,3);
//        java.util.Date date = c.getTime();
//        Date d = new Date(date.getTime());
//
//        String time= df.format(d);
//
//        Timestamp ts= Timestamp.valueOf(time);
//
//        System.out.println(ts);
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        String ret = mockMvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL+"/page/period")
//                        .param("start", String.valueOf(ts))
//
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//
//    @Test
//    void add() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        Bill bill = new Bill();
//        bill.setFigure(new BigDecimal(19.63));
//        bill.setFname("TENNIS");
//        bill.setUid(1);
//        bill.setVname("TT1");
//        bill.setBrid(1);
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Calendar c = Calendar.getInstance();
//        c.set(2023,Calendar.APRIL,3);
//        java.util.Date date = c.getTime();
//        Date d = new Date(date.getTime());
//
//        String time= df.format(d);
//
//        Timestamp ts= Timestamp.valueOf(time);
//        bill.setBdate(ts);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(bill);
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
//                        .get(BASE_URL+"/page/facility")
//                        .param("page", String.valueOf(3))
//                        .param("pageSize",String.valueOf(3))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("ret=======" + ret);
//    }
//}
