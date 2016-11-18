package com.a201csci.shelf_motivation;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BooksAPI{
    private static String API_KEY ="AIzaSyAcIQ5LpzutH12EmFbQjDahAoAgljf7Xgk";
    static String APIurl ="https://www.googleapis.com/books/";
//    public BooksAPI(String key){
//        this.API_KEY = key;
//    }

    public static void getBookByID(Activity activity, String id){
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = APIurl+"v1/volumes/"+id+"?key="+API_KEY;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        //process the response
                        try {
                            System.out.println(response.get("id"));

                            System.out.println(((JSONObject)response.get("volumeInfo")).get("title"));
                            System.out.println(response.get("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }
}


























//
//import android.app.DownloadManager;
//import android.util.Log;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport;
//import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.books.Books;
//import com.google.api.services.books.BooksRequestInitializer;
//import com.google.api.services.books.Books.Volumes.List;
//import com.google.api.services.books.model.Volume;
//import com.google.api.services.books.model.Volumes;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.text.NumberFormat;
///**
// * Created by DELL-PC on 15-Nov-16.
// */
//
//public class BooksAPI {
//    private static final String APPLICATION_NAME = "shelfmotivation";
//    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
//    private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();
////    private static final String API_KEY = "AIzaSyAcIQ5LpzutH12EmFbQjDahAoAgljf7Xgk";
//
//
//    public static void queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception{
////        ClientCredentials.errorIfNotSpecified();
////
////        // Set up Books client.
////        //HttpTransport httpTransport =GoogleNetHttpTransport.newTrustedTransport();
////        Log.i("error",ClientCredentials.API_KEY);
////        final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
////                .setApplicationName(APPLICATION_NAME)
////                .setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
////                .build();
////        // Set query string and filter only Google eBooks.
////        System.out.println("Query: [" + query + "]");
////        List volumesList = books.volumes().list(query);
////        volumesList.setFilter("ebooks");
//
//        String url = "http://my-json-feed";
//
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://www.google.com";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
//            }
//        });
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//// Access the RequestQueue through your singleton class.
////        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
////        // Execute the query.
////        Volumes volumes = volumesList.execute();
////        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
////            System.out.println("No matches found.");
////            return;
////        }
////
////
////        // Output results.
////        for (Volume volume : volumes.getItems()) {
////            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
////            Volume.SaleInfo saleInfo = volume.getSaleInfo();
////            System.out.println("==========");
////            // Title.
////            System.out.println("Title: " + volumeInfo.getTitle());
////            // Author(s).
////            java.util.List<String> authors = volumeInfo.getAuthors();
////            if (authors != null && !authors.isEmpty()) {
////                System.out.print("Author(s): ");
////                for (int i = 0; i < authors.size(); ++i) {
////                    System.out.print(authors.get(i));
////                    if (i < authors.size() - 1) {
////                        System.out.print(", ");
////                    }
////                }
////                System.out.println();
////            }
////            // Description (if any).
////            if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
////                System.out.println("Description: " + volumeInfo.getDescription());
////            }
////            // Link to Google eBooks.
////            System.out.println(volumeInfo.getInfoLink());
////        }
////        System.out.println("==========");
////        System.out.println(
////                volumes.getTotalItems() + " total results at http://books.google.com/ebooks?q="
////                        + URLEncoder.encode(query, "UTF-8"));
//    }
//
////    public static void main(String[] args) {
////        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
////        try {
////            // Verify command line parameters.
////            if (args.length == 0) {
////                System.err.println("Usage: BooksSample [--author|--isbn|--title] \"<query>\"");
////                System.exit(1);
////            }
////            // Parse command line parameters into a query.
////            // Query format: "[<author|isbn|intitle>:]<query>"
////            String prefix = null;
////            String query = "";
////            for (String arg : args) {
////                if ("--author".equals(arg)) {
////                    prefix = "inauthor:";
////                } else if ("--isbn".equals(arg)) {
////                    prefix = "isbn:";
////                } else if ("--title".equals(arg)) {
////                    prefix = "intitle:";
////                } else if (arg.startsWith("--")) {
////                    System.err.println("Unknown argument: " + arg);
////                    System.exit(1);
////                } else {
////                    query = arg;
////                }
////            }
////            if (prefix != null) {
////                query = prefix + query;
////            }
////            try {
////                queryGoogleBooks(jsonFactory, query);
////                // Success!
////                return;
////            } catch (IOException e) {
////                System.err.println(e.getMessage());
////            }
////        } catch (Throwable t) {
////            t.printStackTrace();
////        }
////        System.exit(0);
////    }
//}
//
