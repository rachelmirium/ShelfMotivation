package com.a201csci.shelf_motivation;

import java.util.List;

/**
 * Created by leanntorres1 on 11/17/16.
 */

public class userInformation {

    private String name;
    private boolean doNotDisturb;
    private long lastLogin;
    private List<String> bookshelf;
    private List<goal> goals;
    private List<String> bookclubs;

    public userInformation(String name, String email, long login) {
        this.name = name;
        doNotDisturb = false;
        lastLogin = login;
        bookshelf = null;
        goals = null;
        bookclubs = null;
    }

    // Getters and setters
    public void setName(String n) { name = n; }
    public String getName() { return name; }

    public void setDoNotDisturb(boolean dnd) { doNotDisturb = dnd; }
    public boolean getDoNotDisturb() { return doNotDisturb; }

    public void setLastLogin(long login) { lastLogin = login; }
    public long getLastLogin() { return lastLogin; }

    public void setBookshelf(List<String> shelf) { bookshelf = shelf; }
    public List<String> getBookshelf() { return bookshelf; }

    public void setGoals(List<goal> g) { goals = g; }
    public List<goal> getGoals() { return goals; }

    public void setBookclubs(List<String> bcs) { bookclubs = bcs; }
    public List<String> getBookclubs() { return bookclubs; }

}
