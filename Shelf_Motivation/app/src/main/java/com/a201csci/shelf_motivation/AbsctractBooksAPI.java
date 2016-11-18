package com.a201csci.shelf_motivation;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by DELL-PC on 18-Nov-16.
 */

public interface AbsctractBooksAPI {
    public void gotBookByID(Book book);
    public void gotAllBooks(ArrayList<Book> books);
    public void gotError();
}
