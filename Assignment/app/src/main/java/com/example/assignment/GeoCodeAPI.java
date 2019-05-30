package com.example.assignment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class GeoCodeAPI {

    public static LatLng getUserCordinates(Context context, String address) {
        Locale locale = new Locale("en", "AU");
        Geocoder geocoder = new Geocoder(context, locale);
        List<Address> list;
        LatLng coordinates = null;
        try {
            list = geocoder.getFromLocationName(address, 1);
            coordinates = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
        } catch (Exception e) {
        }
        return coordinates;
    }

    public static JSONArray getParksLocation(LatLng userposition) {
        JSONArray result = null;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + userposition.latitude + "," + userposition.longitude + "&radius=5000&types=park&key=AIzaSyC5sCkeItunYvsnh3vey347LyyrrWLDczI");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(conn.getInputStream());
            String textResult = "";
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            result = new JSONObject(textResult).getJSONArray("results");
        } catch (Exception e) {
        }
        return result;
    }

}
