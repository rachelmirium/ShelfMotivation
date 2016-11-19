package com.a201csci.shelf_motivation;

/**
 * Created by leanntorres1 on 11/17/16.
 */

public class goal {

    private String bookTitle;
    private String date;

    public goal(String bookID, String date) {
        this.bookTitle = bookID;
        this.date = date;
    }

    // Getters and setters
    public void setBookID(String id) { bookTitle = id; }
    public String getBookID() { return bookTitle; }

    public void setGoalDate(String d) { date = d; }
    public String getGoalDate() { return date; }

}
