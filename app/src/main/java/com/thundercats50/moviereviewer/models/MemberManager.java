package com.thundercats50.moviereviewer.models;

import android.app.Application;

import java.util.HashMap;

/**
 * @author Forester Vosburgh
 */
public class MemberManager extends Application {

    private Member currentMember;
    private String currentEmail;

    // backing hashMap to store member models
    public HashMap<String, Member> members = new HashMap<>();

    /**
     * adds a member to the backing hashMap
     * @param member the ember to add
     */
    public void addMember(String email, Member member) {
        members.put(email, member);
        currentMember = member;
        currentEmail = email;
    }

    /**
     * gets a member based on the email
     * @param email the email key to find a user
     * @return the member associated with the email
     */
    public Member getMember(String email) {
        return members.get(email);
    }


    /**
     * updates member by replacing the existing key value pair
     * @param email the email key that will identify the member to update
     * @param member the member model with the apporopriate updated info
     */
    public void updateMember(String email, Member member) {
        members.put(email, member);
    }

    /**
     * sets the current logged in member
     * @param email the key to identify the current member
     */
    public void setCurrentMember(String email) {
        if (getMember(email) == null) {
            User user = new User();
            user.setEmail(email);
            currentMember = new User();
            currentEmail = email;
        } else {
            currentMember = getMember(email);
            currentEmail = email;
        }
    }

    /**
     * gets the current logged in member
     * @return the current logged in member
     */
    public Member getCurrentMember() {
        return currentMember;
    }

    public String getCurrentEmail() {
        return currentEmail;
    }
}
