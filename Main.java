
package com.home;

import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class Main {

    public static String apiKey;
    public static String citySearch;
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

        while (true) {

            System.out.println("Enter a city: ");

            citySearch = scanner.nextLine();

            if (citySearch.equals("exit")) {
                System.out.println("Exiting Program....");
                System.exit(0);
            }

            if (citySearch.isBlank()) {
                System.out.println("ERROR! City cannot be blank!");
                continue;
            }
            
            try {
                
                URI cityURI = new URI(buildAPIURL(citySearch));
                URL cityURL = cityURI.toURL();

                HttpURLConnection mainConnection = (HttpURLConnection) cityURL.openConnection();

                int responseCode = mainConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(mainConnection.getInputStream()));

                    String line; 

                    StringBuffer response = new StringBuffer();

                    while ((line = reader.readLine())!=null) {
                        response.append(line);
                        
                    }
                    
                    parseWeatherData(response.toString());
                    
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    System.out.println("NO CITY FOUND");
                    
                } else{
                    System.out.println("ERROR Fetching eather data");
                }

            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("NO CITY FOUND");
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

    public static String buildAPIURL(String citySearch){

        return String.format(apiURL +"?q=" + citySearch + "&appid=" + apiKey);
        
    }

    public static void parseWeatherData(String getData) {

        try {

            JSONObject json = new JSONObject(getData);

            if (!json.has("weather")) {
                System.out.println("ERROR");
                return;
                
            }

            JSONArray weatherArr = json.getJSONArray("weather");
    
            JSONObject ob1 = weatherArr.getJSONObject(0);
    
            String descriptionData = ob1.getString("description");
    
            System.out.println("Current Conditions: " + descriptionData);
            

        } catch (Exception e) {
            System.out.println("ERROR Parsing weather data");
            e.printStackTrace();
        }



    }

    
}