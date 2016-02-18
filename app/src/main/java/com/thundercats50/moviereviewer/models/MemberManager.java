package com.thundercats50.moviereviewer.models;

import java.util.HashMap;

/**
 * @author Forester Vosburgh
 */
public class MemberManager {

    // backing hashMap to store member models
    private static HashMap<String, Member> members = new HashMap<>();

    /**
     * adds a member to the backing hashMap
     * @param member the ember to add
     */
    public void addMember(Member member) {
        members.put(member.getPassword(), member);
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
}
