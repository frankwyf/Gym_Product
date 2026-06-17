package com.gym.project.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.*;
import com.gym.project.gymmaster.entity.untils.HomeSlides;
import com.gym.project.gymmaster.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/until")
public class UntilController {
    @Autowired
    FacilityService facilityService;

    @Autowired
    VenueService venueService;

    @Autowired
    CoachService coachService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    CourseService courseService;

    @Autowired
    PostsService postsService;

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
            homeSlides.setSlideUrl("http://localhost:8080/until/slides" + i + ".jpg");
            slides[i] = homeSlides;
        }
        log.info("get the slides pictures for the home page");
        return BackMsg.success(slides);
    }

    // get the slides pictures and information for all the facilities
    @GetMapping("/facilities")
    public BackMsg<List<Facility>> DisplayFacilities() {
        // get all the facilities from the database
        LambdaQueryWrapper<Facility> allFacilities = new LambdaQueryWrapper<>();
        // order the facilities by the 'sales' column
        allFacilities.orderByDesc(Facility::getSales);
        // return the facilities
        List<Facility> facilities = facilityService.list(allFacilities);

        log.info("get the slides pictures and information for all the facilities");
        return BackMsg.success(facilities);
    }

    // get all the coaches information
    @GetMapping("/coaches")
    public BackMsg<List<Coach>> DisplayCoaches() {
        // get all the coaches from the database
        LambdaQueryWrapper<Coach> allCoaches = new LambdaQueryWrapper<>();
        // return the coaches
        List<Coach> coaches = coachService.list(allCoaches);
       log.info("get all the coaches information");
       return BackMsg.success(coaches);
    }

    // get all notices
    @GetMapping("/notices")
    public BackMsg<List<Notice>> DisplayNotices() {
        // get all the notices from the database
        LambdaQueryWrapper<Notice> allNotices = new LambdaQueryWrapper<>();
        // return the notices
        List<Notice> notices = noticeService.list(allNotices);
        log.info("get all notices");
        return BackMsg.success(notices);
    }

    // get a specific notice by id
    @GetMapping(value = "/getNotice", params = {"noticeId"})
    public BackMsg<Notice> specificNotice(int noticeId){
        Notice notice = noticeService.getById(noticeId);
        log.info("get a specific notice by id");
        return BackMsg.success(notice);
    }

    // get the slides for course page
    @GetMapping("/courseSlides")
    public BackMsg<HomeSlides[]> CourseSlides() {
        HomeSlides [] slides = new HomeSlides[7];
        // description of the slide pictures
        String[] text = {"General","Strength","Flexibility","Football","Basketball","Tennis","Group activity"};
        for (int i = 0; i < 7; i++) {
            HomeSlides homeSlides = new HomeSlides();
            homeSlides.setSlideID(i);
            homeSlides.setText(text[i]);
            homeSlides.setSlideUrl("http://localhost:8080/until/course" + i + ".jpg");
            slides[i] = homeSlides;
        }
        log.info("get the slides for course page");
        return BackMsg.success(slides);
    }

    // get all courses for the course page
    @GetMapping("/allCourses")
    public BackMsg<List<Course>> DisplayCourses() {
        // get all the courses from the database
        LambdaQueryWrapper<Course> allCourses = new LambdaQueryWrapper<>();
        // return the courses
        List<Course> courses = courseService.list(allCourses);
        log.info("get all courses for the course page");
        return BackMsg.success(courses);
    }

    // get information of a specific course by course id
    @GetMapping(value = "/specificCourse", params = {"courseID"})
    public BackMsg<Course> getSpecificCourse(int courseID) {
        Course course = courseService.getById(courseID);
        log.info("get information of a specific course by course id");
        return BackMsg.success(course);
    }

    // get all the post from the database
    @GetMapping("/allPosts")
    public BackMsg<List<Posts>> DisplayPosts() {
        // get all the posts from the database
        LambdaQueryWrapper<Posts> allPosts = new LambdaQueryWrapper<>();
        // return the posts
        List<Posts> posts = postsService.list(allPosts);
        log.info("get all the post from the database");
        return BackMsg.success(posts);
    }

    // get a specific facility information by the facility id
    @GetMapping (value = "/specificFacility", params = {"facilityID"})
    public BackMsg<Facility> getSpecificFacility(int facilityID) {
        Facility facility = facilityService.getById(facilityID);
        log.info("get a specific facility information by the facility id");
        return BackMsg.success(facility);
    }

    // get a specific facility's venues by the facility id
    @GetMapping (value = "/venuesInfo", params = {"facilityID"})
    public BackMsg<List<Venue>> getVenuesInfo(int facilityID) {
        // get all the venues from the database
        LambdaQueryWrapper<Venue> allVenues = new LambdaQueryWrapper<>();
        // return the venues
        allVenues.eq(Venue::getFid, facilityID);
        List<Venue> venues = venueService.list(allVenues);
        log.info("get a specific facility's venues by the facility id");
        return BackMsg.success(venues);
    }
}
