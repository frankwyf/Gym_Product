package com.gymmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenCap {
    private Venue venue;
    private Date date;
    private int[] cap;

    // constructor with only venue and cap
    public VenCap(Venue venue, int[] cap) {
        this.venue = venue;
        this.cap = cap;
    }
}

