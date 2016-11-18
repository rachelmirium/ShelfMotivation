package com.a201csci.shelf_motivation;

/**
 * Created by DELL-PC on 16-Nov-16.
 */
public class ClientCredentials {

    /** Value of the "API key" shown under "Simple API Access". */
    static final String API_KEY =
            "AIzaSyAcIQ5LpzutH12EmFbQjDahAoAgljf7Xgk"
                    + ClientCredentials.class;

    static void errorIfNotSpecified() {
        if (API_KEY.startsWith("Enter ")) {
            System.err.println(API_KEY);
            System.exit(1);
        }
    }
}
