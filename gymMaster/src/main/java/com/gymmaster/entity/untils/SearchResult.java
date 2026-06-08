package com.gymmaster.entity.untils;

import com.gymmaster.entity.Course;
import com.gymmaster.entity.Facility;
import com.gymmaster.entity.Venue;
import lombok.Data;

import java.util.ArrayList;

@Data
public class SearchResult {
    private ArrayList<Facility> facilities;
    private ArrayList<Venue> venues;
    private ArrayList<Course> courses;
}
