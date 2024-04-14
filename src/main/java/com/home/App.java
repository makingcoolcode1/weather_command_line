
package com.home;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class App {

    public static String apiUrl = "https://api.openweathermap.org/data/2.5/weather";
    public static String apiKey;
    public static String cityInput;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n**Welcome to weather search**");
        System.out.println("You may type 'exit' at anytime to quit the program");
        System.out.println("\nPlease enter your API key from openweathermap.org: ");

        boolean apiKeyValidation = false;

        while (!apiKeyValidation) {
            
            apiKey = scanner.nextLine();

            if (apiKey.equals("exit")) {
                System.out.println("\nExiting Program.........");
                break;
            }

            if (apiKey.isBlank()) {
                System.out.println("ERROR: API Key cannot be blank.....Please enter a valid API key:");
                continue;
            }

            if (testApiKey(apiKey)) {
                System.out.println("API Key Validated");
                apiKeyValidation = true;
            } else {
                System.out.println("ERROR: API key cannot be valdated....Please enter a valid API key: ");
            }

        }

        while (true) {
            System.out.println("\nEnter a city");

            cityInput = scanner.nextLine();

            if (cityInput.equals("exit")) {
                System.out.println("Exiting Program.....");
                break;
            }

            if (cityInput.isBlank()) {
                System.out.println("ERROR: Input cannot be blank......Please enter a city:");
                continue;
            }

            try {
                
                URI cityUri = new URI(buildApiUrl(cityInput));
                URL cityUrl = cityUri.toURL();

                HttpURLConnection cityConnection = (HttpURLConnection) cityUrl.openConnection();

                if (cityConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(cityConnection.getInputStream()));

                    String line;

                    StringBuffer response = new StringBuffer();

                    while ((line = reader.readLine())!=null) {
                        response.append(line);
                    }

                    parseWeatherData(response.toString());
                    
                } else {
                    System.out.println("ERROR: Unable to connect to API");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }

    public static boolean testApiKey(String apiKey) {

        try {
            
            URI uri = new URI(apiUrl + "?q=Baltimore&appid=" + apiKey );
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int apiTest = connection.getResponseCode();

            return apiTest == 200;
            
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    public static String buildApiUrl(String cityInput){
        return String.format("%s?q=%s&appid=%s&units=metric",apiUrl, cityInput ,apiKey);
    }

    public static void parseWeatherData(String getWeatherData){

        JSONObject json = new JSONObject(getWeatherData);

        JSONObject main = json.getJSONObject("main");

        int temp = main.getInt("temp");

        System.out.println("\nTemperature: " + temp);



    }
}