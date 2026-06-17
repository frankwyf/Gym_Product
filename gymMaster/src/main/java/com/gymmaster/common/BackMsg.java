package com.gymmaster.common;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * a class to determine the return message from the server
 * @param <T>
 */
@Data
public class BackMsg<T> {

    private Integer code; // self defined status code, 1 means success, 0 means error

    private String msg; // if the code is 0, the msg will be returned

    private T data; // data from database

    private Map<String, Object> map = new HashMap<>(); // data structure to store data

    /**
     * success function
     * @param object
     * @param <T>
     * @return
     */
    public static <T> BackMsg<T> success(T object) {
        BackMsg<T> r = new BackMsg<>();
        r.data = object;
        r.code = 1; // success
        return r;
    }

    public static <T> BackMsg<T> error(String msg) {
        BackMsg<T> r = new BackMsg<>();
        r.msg = msg;
        r.code = 0; // error
        return r;
    }
    public static <T> BackMsg<T> error(Integer code,String msg) {
        BackMsg<T> r = new BackMsg<>();
        r.msg = msg;
        r.code = code; // error
        //r.data = data;
        return r;
    }
    public BackMsg<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
