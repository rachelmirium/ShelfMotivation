package com.a201csci.shelf_motivation;

/**
 * Created by leanntorres1 on 11/17/16.
 */

public class goal {

    private String bookID;
    private String date;

    public goal(String bookID, String date) {
        this.bookID = bookID;
        this.date = date;
    }

    // Getters and setters
    public void setBookID(String id) { bookID = id; }
    public String getBookID() { return bookID; }

    public void setGoalDate(String d) { date = d; }
    public String getGoalDate() { return date; }

}
