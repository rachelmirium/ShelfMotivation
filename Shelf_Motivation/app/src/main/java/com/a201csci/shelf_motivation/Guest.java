package com.a201csci.shelf_motivation;

/**
 * Created by Katie on 11/15/16.
 */


import android.app.Application;

public class Guest extends Application {

    private boolean isGuest;

    public boolean getGuest() {
        return isGuest;
    }

    public void setGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }

}
