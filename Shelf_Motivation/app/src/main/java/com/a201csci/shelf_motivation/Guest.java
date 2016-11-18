package com.a201csci.shelf_motivation;

/**
 * Created by Katie on 11/15/16.
 */


import android.app.Application;

public class Guest extends Application {

    private boolean isGuest;
    private int numberOfBooks;

    public boolean getGuest() {
        return isGuest;
    }

    public void setGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }

    public int getNumberOfBooks(){
        return numberOfBooks;
    }


    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

}
