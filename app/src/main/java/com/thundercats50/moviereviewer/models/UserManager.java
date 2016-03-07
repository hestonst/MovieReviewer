package com.thundercats50.moviereviewer.models;

import android.app.Application;

import java.util.HashMap;

/**
 * @author Forester Vosburgh
 */
public class UserManager extends Application {

    private User currentMember;

    /**
     * adds a member to the backing hashMap
     * @param member the ember to add
     */
    public void setCurrentMember(User member) {
        currentMember = member;
    }

    /**
     * gets a member based on the email
     * @return the user associated with the email
     */
    public User getCurrentMember() {
        return currentMember;
    }

}
