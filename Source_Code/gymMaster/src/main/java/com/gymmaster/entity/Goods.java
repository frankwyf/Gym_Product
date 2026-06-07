package com.gymmaster.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Goods implements Serializable {
    Reservation reservation;
    int amount;
    int venue;
    int price;
    String name;
    String type;
    String date;
    String pic;
    int facility;
    String period;
    Boolean active;
}
