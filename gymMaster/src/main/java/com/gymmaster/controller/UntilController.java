package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.*;
import com.gymmaster.entity.untils.SearchResult;
import com.gymmaster.entity.untils.VenueSlides;
import com.gymmaster.service.*;
import com.gymmaster.entity.untils.HomeSlides;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Autowired
    CommentService commentService;

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
            homeSlides.setSlideUrl("slides" + i + ".jpg");
            slides[i] = homeSlides;
        }
        log.info("get the slides pictures for the home page");
        return BackMsg.success(slides);
    }

    // get the slides pictures and information for all the facilities
    @GetMapping("/facilities")
    public BackMsg<ArrayList<Facility>> DisplayFacilities() {
        // get all the facilities from the database
        LambdaQueryWrapper<Facility> allFacilities = new LambdaQueryWrapper<>();
        // order the facilities by the 'sales' column
        allFacilities.orderByDesc(Facility::getSales);
        // return the facilities
        ArrayList<Facility> facilities = new ArrayList<>(facilityService.list(allFacilities));

        log.info("get the slides pictures and information for all the facilities");
        return BackMsg.success(facilities);
    }

    // get all the coaches information
    @GetMapping("/coaches")
    public BackMsg<ArrayList<Coach>> DisplayCoaches() {
        // get all the coaches from the database
        LambdaQueryWrapper<Coach> allCoaches = new LambdaQueryWrapper<>();
        // return the coaches
        ArrayList<Coach> coaches = new ArrayList<>(coachService.list(allCoaches));
       log.info("get all the coaches information");
       return BackMsg.success(coaches);
    }

    // get all notices
    @GetMapping("/notices")
    public BackMsg<ArrayList<Notice>> DisplayNotices() {
        // get all the notices from the database
        LambdaQueryWrapper<Notice> allNotices = new LambdaQueryWrapper<>();
        // return the notices
        ArrayList<Notice> notices = new ArrayList<>(noticeService.list(allNotices));
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
            homeSlides.setSlideUrl("course" + i + ".jpg");
            slides[i] = homeSlides;
        }
        log.info("get the slides for course page");
        return BackMsg.success(slides);
    }

    // get all courses for the course page
    @GetMapping("/allCourses")
    public BackMsg<ArrayList<Course>> DisplayCourses() {
        // get all the courses from the database
        LambdaQueryWrapper<Course> allCourses = new LambdaQueryWrapper<>();
        // return the courses
        ArrayList<Course> courses = new ArrayList<>(courseService.list(allCourses));
        log.info("get all courses for the course page");
        return BackMsg.success(courses);
    }

    // add a course
    @GetMapping(value = "/addCourse", params = {"newCourse"})
    public BackMsg<String> addCourse(@RequestBody Course newCourse){
        try{
            courseService.save(newCourse);
        }catch (Exception e){
            log.error("Failed to add course", e);
            return BackMsg.error(e.toString());
        }
        return BackMsg.success("Successfully add course");
    }

    // delete course by course ID
    @DeleteMapping("/Course/Delete/{couid}")
    public BackMsg<String> deleteCourse(@PathVariable int couid){
        try{
            LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Course::getCouid, couid);
            courseService.remove(queryWrapper);
        }catch (Exception e){
            log.error("Failed to delete course {}", couid, e);
            return BackMsg.error(e.toString());
        }
        return BackMsg.success("Successfully delete course" + couid);
    }

    // update information of course by course ID
    @PutMapping(value = "/updateCourse")
    public BackMsg<String> updateCourse(@RequestBody Course newCourse){
        try{
            LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Course::getCouid,newCourse.getCouid());
            courseService.update(newCourse,queryWrapper);
        }catch (Exception e){
            log.error("Failed to update course {}", newCourse.getCouid(), e);
            return BackMsg.error(e.toString());
        }
        return BackMsg.success("Successfully update course message!");
    }

    // get information of a specific course by course-venue
    @GetMapping(value = "/Course/VenueCourse", params = {"courseVenue"})
    public BackMsg<ArrayList<Course>> searchCourseByVenue(int courseVenue){
        LambdaQueryWrapper<Course> venueCourses = new LambdaQueryWrapper<>();
        venueCourses.eq(Course::getCourseVenue, courseVenue);
        ArrayList<Course> courses = new ArrayList<>(courseService.list(venueCourses));
        log.info("get courses which are this venue from the database");
        return BackMsg.success(courses);
    }

    //get courses by type
    @GetMapping(value = "/Course/Type", params = {"courseType"})
    public BackMsg<ArrayList<Course>> searchCourseByType(String courseType){
        LambdaQueryWrapper<Course> venueCourses = new LambdaQueryWrapper<>();
        venueCourses.eq(Course::getType, courseType);
        ArrayList<Course> courses = new ArrayList<>(courseService.list(venueCourses));
        log.info("get courses which are this type from the database");
        return BackMsg.success(courses);
    }

    //get information of list of course by course coaid
    @GetMapping(value = "/Course/coaid", params = {"coAid"})
    public BackMsg<ArrayList<Course>> searchCourseByCoaid(int coAid){
        LambdaQueryWrapper<Course> venueCourses = new LambdaQueryWrapper<>();
        venueCourses.eq(Course::getCoaid, coAid);
        ArrayList<Course> courses = new ArrayList<>(courseService.list(venueCourses));
        log.info("get courses which are this coach from the database");
        return BackMsg.success(courses);
    }

    // get information of list of courses by range of this price
    @GetMapping(value = "/Course/Price", params = {"max", "min"})
    public BackMsg<ArrayList<Course>> searchCourseByPrice(int max, int min){
        LambdaQueryWrapper<Course> venueCourses = new LambdaQueryWrapper<>();
        venueCourses.between(Course::getPrice, min, max);
        ArrayList<Course> courses = new ArrayList<>(courseService.list(venueCourses));
        log.info("get courses which are this price in this range from the database");
        return BackMsg.success(courses);
    }

    // get information of a specific course by course id
    @GetMapping(value = "/specificCourse", params = {"courseID"})
    public BackMsg<CourseCoach> getSpecificCourse(int courseID) {
        Course course = courseService.getById(courseID);
        // also get the coach information
        Coach coach = coachService.getById(course.getCoaid());
        // put the course and coach information into the entity
        CourseCoach map = new CourseCoach();
        map.setCourse(course);
        map.setCoach(coach);
        log.info("get information of a specific course by course id");
        return BackMsg.success(map);
    }

    // get all the post from the database
    @GetMapping("/allPosts")
    public BackMsg<ArrayList<Posts>> DisplayPosts() {
        // get all the posts from the database
        LambdaQueryWrapper<Posts> allPosts = new LambdaQueryWrapper<>();
        // return the posts
        ArrayList<Posts> posts = new ArrayList<>(postsService.list(allPosts));
        log.info("get all the post from the database");
        return BackMsg.success(posts);
    }

    // get a specific post by post id
    @GetMapping(value = "/specificPost", params = {"postID"})
    public BackMsg<Posts> getSpecificPost(int postID) {
        Posts post = postsService.getById(postID);
        log.info("get a specific post by post id");
        return BackMsg.success(post);
    }

    // get the comments of a specific post by post id
    @GetMapping(value = "/postComment", params = {"postID"})
    public BackMsg<ArrayList<Comments>> getPostComment(int postID) {
        // get all the comments from the database
        LambdaQueryWrapper<Comments> allComments = new LambdaQueryWrapper<>();
        // get the comments of a specific post by post id
        allComments.eq(Comments::getPid, postID);
        // return the comments
        ArrayList<Comments> comments = new ArrayList<>(commentService.list(allComments));
        log.info("get the comments of a specific post by post id");
        return BackMsg.success(comments);
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
    public BackMsg<ArrayList<Venue>> getVenuesInfo(int facilityID) {
        // get all the venues from the database
        LambdaQueryWrapper<Venue> allVenues = new LambdaQueryWrapper<>();
        // return the venues
        allVenues.eq(Venue::getFid, facilityID);
        ArrayList<Venue> venues = new ArrayList<>(venueService.list(allVenues));
        log.info("get a specific facility's venues by the facility id");
        return BackMsg.success(venues);
    }

    // get all the venues name and picture
    @GetMapping("/venuesSlides")
    public BackMsg<VenueSlides[]> getVenuesSlides() {
        // get all the names of the venues from the database
        LambdaQueryWrapper<Venue> allVenues = new LambdaQueryWrapper<>();
        // order by the venue id
        allVenues.orderByAsc(Venue::getVid);
        List<Venue> venueList = venueService.list(allVenues);
        // put the names and media into a map
        Map<String, String> map = new LinkedHashMap<>();
        // get the ids of all the venues
        ArrayList<Integer> ids = new ArrayList<>();
        for (Venue venue : venueList) {
            ids.add(venue.getVid());
            map.put(venue.getVname(), venue.getProfile());
        }
        VenueSlides [] slides = new VenueSlides[map.size()];
        // get all the pictures and name in the map and put them into the slides
        for (int i = 0; i < map.size(); i++) {
            VenueSlides venueSlides = new VenueSlides();
            venueSlides.setVid(ids.get(i));
            venueSlides.setName((String) map.keySet().toArray()[i]);
            venueSlides.setPicUrl((String) map.values().toArray()[i]);
            slides[i] = venueSlides;
        }
        log.info("get all the venues name and picture");
        return BackMsg.success(slides);
    }
    // search for facilities or venues or courses by the name
    // blur search is used
    @GetMapping(value = "/search", params = {"name"})
    public BackMsg<SearchResult> search(String name) {
        // get all the facilities from the database
        LambdaQueryWrapper<Facility> allFacilities = new LambdaQueryWrapper<>();
        // return the facilities
        allFacilities.like(Facility::getFname, name);
        ArrayList<Facility> facilities = new ArrayList<>(facilityService.list(allFacilities));
        // get all the venues from the database
        LambdaQueryWrapper<Venue> allVenues = new LambdaQueryWrapper<>();
        // return the venues
        allVenues.like(Venue::getVname, name);
        ArrayList<Venue> venues = new ArrayList<>(venueService.list(allVenues));
        // get all the courses from the database
        LambdaQueryWrapper<Course> allCourses = new LambdaQueryWrapper<>();
        // return the courses
        allCourses.like(Course::getType, name);
        ArrayList<Course> courses = new ArrayList<>(courseService.list(allCourses));
        // put the facilities and venues into the search entity
        SearchResult searchResult = new SearchResult();
        // put the facilities into the search entity
        // the first private variable the array is all the resulting facilities
        searchResult.setFacilities(facilities);
        // put the venues into the search entity
        // the second private variable the array is all the resulting venues
        searchResult.setVenues(venues);
        // put the courses into the search entity
        // the third private variable the array is all the resulting courses
        searchResult.setCourses(courses);
        log.info("search for facilities or venues by the name");
        return BackMsg.success(searchResult);
    }
}
