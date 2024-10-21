

package com.home;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Main {

    public static String apiURL = "https://api.openweathermap.org/data/2.5/weather";
    public static String apiKey;
    public static String citySearch;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nWelcome to the weather app!");
        System.out.println("\nYou must sign up for a free API key from openweathermap.org to proceed.....");

        boolean apiKeyValidated = false;

        while (!apiKeyValidated) {

            System.out.println("\nPlease enter your API key: ");

            apiKey = scanner.nextLine();

            if (apiKey.equals("exit")) {
                System.out.println("\nExiting Program....");
                System.exit(0);
            }

            if (apiKey.isBlank()) {
                System.out.println("\nERROR! API Key cannot be blank!");
                continue;
            }

            if (validateKey(apiKey)) {

                System.out.println("\nAPI KEY VALID");
                apiKeyValidated = true;
            } else {
                System.out.println("ERROR! Failed to validate API Key.");
            }
            
        }


        while (true) {
            System.out.println("\nPlease enter a city: ");

            citySearch = scanner.nextLine();

            try {
                
                URI mainURI = new URI(buildAPIURL(citySearch));
                URL mainURL = mainURI.toURL();

                HttpURLConnection mainConnection = (HttpURLConnection) mainURL.openConnection();

                int mainResponse = mainConnection.getResponseCode();

                if (mainResponse == (HttpURLConnection.HTTP_OK)) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(mainConnection.getInputStream()));

                    String line;

                    StringBuffer response = new StringBuffer();

                    while ((line = reader.readLine())!=null) {
                        response.append(line);
                        
                    }

                    parseWeatherData(response.toString());
                    
                } else if (mainResponse == 404) {
                    System.out.println("ERROR! No city found");
                    
                } 


            } catch (Exception e) {
                System.out.println("ERROR! Failed to connect to API!");
            }
            
        }

    }

    private static void parseWeatherData(String getData) {

        JSONObject json = new JSONObject(getData);

        JSONArray weatherArr = json.getJSONArray("weather");

        JSONObject object1 = weatherArr.getJSONObject(0);

        String description = object1.getString("description");

        System.out.println("Description: " + description);
        
    }

    private static String buildAPIURL(String citySearch) {
        return String.format(apiURL + "?q=" + citySearch + "&appid=" + apiKey);
    }

    private static boolean validateKey(String apiKey) {

        try {
            
            URI testURI = new URI(apiURL + "?q=Baltimore&appid=" + apiKey);
            URL testURL = testURI.toURL();

            HttpURLConnection testConnect = (HttpURLConnection) testURL.openConnection();

            testConnect.setRequestMethod("GET");

            int testResponse = testConnect.getResponseCode();

            return testResponse == 200;

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

  
}