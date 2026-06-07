package com.gymmaster.common;

/**
 * a class to set the user id into the thread
 */
public class ThreadContext {
    private static ThreadLocal<Integer> threadID = new ThreadLocal<>(); // stored user ID
    private static ThreadLocal<String> threadType = new ThreadLocal<>(); // store user type (customer, coach, employee,manager)


    public static void setCurrentId(int id){
        threadID.set(id);
    }
    public static void setCurrentType(String type){
        threadType.set(type);
    }

    public static Integer getCurrentId(){
        return threadID.get();
    }
    public static String getCurrentType(){
        return threadType.get();
    }


}
