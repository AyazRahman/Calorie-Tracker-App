package com.example.assignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchGoogleApi {
    private static final String API_KEY = "AIzaSyD0voxCAVywyoXU4cdIEJpehHrSZtSqj5E";
    //private static final String API_KEY = "AIzaSyC5KDVOooy4B6xrDRgF7ggwtv8wzoU2h1s";
    private static final String SEARCH_ID_cx = "002371663703481811589:5f2ckyq-kxq";

    public static String search(String keyword, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String imageResult = "";
        String query_parameter = "";

        if (params != null && values != null) {
            for (int i = 0; i < params.length; i++) {
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + SEARCH_ID_cx + "&q=" + keyword + query_parameter);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return textResult;
    }

    public static String getSnippet(String result) {
        String snippet = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray != null && jsonArray.length() > 0) {
                snippet = jsonArray.getJSONObject(0).getString("snippet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }


    public static Bitmap getImage(String result) {
        Bitmap image = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray != null && jsonArray.length() > 0) {
                String im = jsonArray.getJSONObject(0).getJSONObject("image").getString("thumbnailLink");
                image = BitmapFactory.decodeStream((InputStream) new URL(im).getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return image;
    }
}
