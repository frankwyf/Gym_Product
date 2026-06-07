package com.gym.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.gymmaster.common.BackMsg;
import com.gym.gymmaster.entity.Coach;
import com.gym.gymmaster.entity.Facility;
import com.gym.gymmaster.entity.untils.HomeSlides;
import com.gym.gymmaster.service.CoachService;
import com.gym.gymmaster.service.FacilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/until")
public class UntilController {
    @Autowired
    FacilityService facilityService;

    @Autowired
    CoachService coachService;

    // get the slides pictures for the home page
    @GetMapping("/homeslides")
    public BackMsg<HomeSlides[]> HomeSlides() {
        HomeSlides [] slides = new HomeSlides[5];
        // description of the slide pictures
        String[] text = {"GymMaster indoor Gym", "Join Us now !", "Outdoor Sports centre", "Our Tennis court", "Our Basketball court"};
        for (int i = 0; i < 5; i++) {
            HomeSlides homeSlides = new HomeSlides();
            homeSlides.setSlideID(i);
            homeSlides.setText(text[i]);
            homeSlides.setSlideUrl("http://localhost:8888/until/slides" + i + ".jpg");
            slides[i] = homeSlides;
        }
        log.info("get the slides pictures for the home page");
        return BackMsg.success(slides);
    }

    // get the slides pictures and information for all the facilities
    @GetMapping("/facilities")
    public BackMsg<ArrayList<Facility>> DisplayFacilities() {
        // load all the facilities from the database, length of the array is not fixed
        ArrayList<Facility> facilities = new ArrayList<>();
        // get all the facilities from the database
        LambdaQueryWrapper<Facility> allFacilities = new LambdaQueryWrapper();
        // return the facilities
        facilities = (ArrayList<Facility>) facilityService.list(allFacilities);

        log.info("get the slides pictures and information for all the facilities");
        return BackMsg.success(facilities);
    }

    // get all the coaches information
    @GetMapping("/coaches")
    public BackMsg<ArrayList<Coach>> DisplayCoaches() {
        // load all the coaches from the database, length of the array is not fixed
        ArrayList<Coach> coaches = new ArrayList<>();
        // get all the coaches from the database
        LambdaQueryWrapper<Coach> allCoaches = new LambdaQueryWrapper();
        // return the coaches
        coaches = (ArrayList<Coach>) coachService.list(allCoaches);
       log.info("get all the coaches information");
       return BackMsg.success(coaches);
    }
}
