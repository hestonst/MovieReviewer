package com.thundercats50.moviereviewer.models;

import android.app.Application;


/**
 * @author Forester Vosburgh
 */
public class UserManager extends Application {

    private static User currentMember = new User("","","","","");
    //so app does not crash if DB connection unavailable

    /**
     * adds a member to the backing hashMap
     * @param member the ember to add
     */
    public static void setCurrentMember(User member) {
        currentMember = member;
    }

    /**
     * gets a member based on the email
     * @return the user associated with the email
     */
    public static User getCurrentMember() {
        return currentMember;
    }

}
