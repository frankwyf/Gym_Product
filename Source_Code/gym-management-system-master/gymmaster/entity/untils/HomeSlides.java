package com.gym.gymmaster.entity.untils;

import lombok.Data;

@Data
public class HomeSlides {
    public int slideID; // the order of the slide picture
    private String text; // textual description of the slide picture
    private String slideUrl; // url of the slide picture
}
