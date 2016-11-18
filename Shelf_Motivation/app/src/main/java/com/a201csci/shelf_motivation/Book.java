package com.a201csci.shelf_motivation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL-PC on 17-Nov-16.
 */

public class Book {
    private String bookID;
    private String title;
    private String description;
    private ArrayList<String> authors;
    private String imageURL;

    public Book(JSONObject bookInfo){
        try {
            System.out.println("hello");
            //set book id
            this.bookID = bookInfo.get("id").toString();

            JSONObject volumeInfo = (JSONObject)bookInfo.get("volumeInfo");
            //set title and description
            this.title = volumeInfo.get("title").toString();
            if(volumeInfo.has(description))
                this.description = volumeInfo.get("description").toString();
            else
                this.description = "Sorry, no description available for this title.";

            //set list of authors
            authors = new ArrayList<String>();
            JSONArray tempAuthors = (JSONArray) volumeInfo.get("authors");
            String[] allauthors = getStringArray(tempAuthors);
            for(String s : allauthors){
                authors.add(s);
            }

            //set image url
            JSONObject imageLinks = (JSONObject)volumeInfo.get("imageLinks");
            this.imageURL = imageLinks.get("thumbnail").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getImageURL() {
        return imageURL;
    }

    //source http://stackoverflow.com/questions/15871309/convert-jsonarray-to-string-array
    private String[] getStringArray(JSONArray jsonArray){
        String[] stringArray = null;
        int length = jsonArray.length();
        if(jsonArray!=null){
            stringArray = new String[length];
            for(int i=0;i<length;i++){
                stringArray[i]= jsonArray.optString(i);
            }
        }
        return stringArray;
    }

}
