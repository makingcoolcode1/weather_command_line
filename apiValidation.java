
package com.home;

import java.net.URL;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URI;

public class apiValidation {

    public static String apiKey;
    public static String apiURL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n**WELCOME TO THE WEATHER APP**");
        System.out.println("\nYou must sign up for a free API key from openweathermap.org to access the app!");

        boolean APIKeyValidated = false;

        while (!APIKeyValidated) {

            System.out.println("\nPlease enter your API Key: ");

            apiKey = scanner.nextLine();

            if (apiKey.equals("exit")) {
                System.out.println("Exiting Program....");
                System.exit(0);
                
            }

            if (apiKey.isBlank()) {
                System.out.println("ERROR! API key cannot be blank!");
                continue;
            }

            if (vaidateAPIKey(apiKey)) {
                
                System.out.println("\nAPI KEY VALIDATD!");
                APIKeyValidated = true;
            } else {
                System.out.println("\nERROR! Failed to validate API key. Please check your entry or enter another key. ");
            }
            
        }

    }

    public static boolean vaidateAPIKey(String apiKey) {

        try {
            
            URI testURI = new URI(apiURL + "?q=Baltimore&appid=" + apiKey);
            URL testURL = testURI.toURL();

            HttpURLConnection testConnect = (HttpURLConnection) testURL.openConnection();

            testConnect.setRequestMethod("GET");

            int testRequest = testConnect.getResponseCode();

            return testRequest == 200;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
        
    }

    
}