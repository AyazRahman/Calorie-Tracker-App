package com.example.assignment;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class RestClient {

    static final String BASE_URL = "http://10.0.2.2:8080/BackendProject/webresources/";

    private static String getPath(String entity) {
        String result = "";
        switch (entity) {
            case "Users":
                result = "backendpkg.users/";
                break;
            case "Credentials":
                result = "backendpkg.credentials/";
                break;
            case "Report":
                result = "backendpkg.report/";
                break;
            case "Food":
                result = "backendpkg.food/";
                break;
            case "Consumption":
                result = "backendpkg.consumption/";
                break;

        }
        return result;
    }


    public static HashMap<String, String> login(String username, String password) {
        final String methodPath = getPath("Credentials") + "findByUsernameAndPassword/" + username + "/" + password;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";

        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("username", username);
        try {
            // JSONObject jsonObject = new JSONObject(textResult);
            JSONArray jsonArray = new JSONArray(textResult);
            if (jsonArray != null && jsonArray.length() > 0) {
                //Add results that might be needed in later steps
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                result.put("name", jsonObject.getJSONObject("userid").getString("name"));
                result.put("id", jsonObject.getJSONObject("userid").getString("userid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static Integer getMaxID(String entity) {
        final String methodPath = getPath(entity) + "getMaxID";
        URL url = null;
        String textResult = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");

//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return Integer.parseInt(textResult);
    }

    public static boolean signUp(Users user, Credentials credential) {
        String methodPath;
        URL url = null;
        HttpURLConnection conn = null;

        Integer id = getMaxID("Users") + 1;
        user.setUserid(id);


        try {
            methodPath = getPath("Users");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUserJson = gson.toJson(user);
            url = new URL(BASE_URL + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
//add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            conn.disconnect();
        }

        try {
            credential.setUserid(user);
            methodPath = getPath("Credentials");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUserJson = gson.toJson(credential);
            url = new URL(BASE_URL + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
//add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            conn.disconnect();
        }
        return true;
    }

    public static Users getUser(int id) {
        Users result = new Users();
        final String methodPath = getPath("Users") + id;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        result = extractUsers(textResult);
        return result;
    }

    private static Users extractUsers(String textResult) {
        Users result = new Users();
        try {
            JSONObject jsonObject = new JSONObject(textResult);
            result.setUserid(jsonObject.getInt("userid"));
            result.setName(jsonObject.getString("name"));
            result.setSurname(jsonObject.getString("surname"));
            result.setStepspermile(jsonObject.getInt("stepspermile"));
            result.setWeight(new BigDecimal(String.format("%.02f", jsonObject.getDouble("weight"))));
            result.setHeight(new BigDecimal(String.format("%.02f", jsonObject.getDouble("height"))));
            result.setPostcode(jsonObject.getString("postcode"));
            result.setLevelofactivity(jsonObject.getString("levelofactivity"));
            result.setGender(jsonObject.getString("gender"));
            result.setEmail(jsonObject.getString("email"));
            result.setAddress(jsonObject.getString("address"));
            result.setDob(new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("dob").substring(0, 10)));
        } catch (Exception e) {
        }
        return result;
    }


    public static void postReport(Report userReport, int userid) {
        userReport.setReportid(getMaxID("Report") + 1);
        userReport.setUserid(getUser(userid));
        String requestType = "POST";
        String methodPath = getPath("Report");
        URL url = null;
        HttpURLConnection conn = null;


        try {

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUserJson = gson.toJson(userReport);
            url = new URL(BASE_URL + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod(requestType);
            //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
//add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }


    }


    public static String getTotalCalorieConsumed(int userid) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());

        final String methodPath = getPath("Consumption") + "getTotalCalorie/" + userid + "/" + date;
        URL url = null;
        String textResult = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getCalorieBurnedRest(int userid) {

        final String methodPath = getPath("Users") + "getCaloriesBurnedAtRest/" + userid;
        URL url = null;
        String textResult = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getCalorieBurnedPerStep(int userid) {

        final String methodPath = getPath("Users") + "getCaloriesBurnedPerStep/" + userid;
        URL url = null;
        String textResult = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String pieData(String date, int userid) {
        String methodPath = getPath("Report") + "getCalorieDetails/" + userid + "/" + date;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String barData(String start, String end, int userid) {
        String methodPath = getPath("Report") + "getTotalReports/" + userid + "/" + start + "/" + end;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static ArrayList<String> getFoodCategory() {
        ArrayList<String> result = new ArrayList<String>();
        String methodPath = getPath("Food") + "getCategory";
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        try {
            JSONArray jsonArray = new JSONArray(textResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static ArrayList<Food> getFoodItems(String category) {
        ArrayList<Food> result = new ArrayList<Food>();
        String methodPath = getPath("Food") + "findByCategory/" + category;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        try {
            JSONArray jsonArray = new JSONArray(textResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(extractFood(jsonArray.getJSONObject(i)));
            }
        } catch (Exception e) {
        }
        return result;
    }

    private static Food extractFood(JSONObject item) {
        Food result = new Food();
        try {
            result.setFoodid(item.getInt("foodid"));
            result.setName((item.getString("name")));
            result.setCategory((item.getString("category")));
            result.setCalorieamount(new BigDecimal(String.format("%.02f", item.getDouble("calorieamount"))));
            result.setFat(new BigDecimal(String.format("%.02f", item.getDouble("fat"))));
            result.setServingunit(item.getString("servingunit"));
            result.setServingamount(new BigDecimal(String.format("%.02f", item.getDouble("servingamount"))));
        } catch (Exception e) {
        }

        return result;
    }

    public static void postConsumption(Consumption newConsumption) {
        newConsumption.setConsumptionid(getMaxID("Consumption") + 1);
        String requestType = "POST";
        String methodPath = getPath("Consumption");
        URL url = null;
        HttpURLConnection conn = null;

        try {

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUserJson = gson.toJson(newConsumption);
            url = new URL(BASE_URL + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod(requestType);
            //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
//add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
    }

    public static void postFood(Food newFood) {
        newFood.setFoodid(getMaxID("Food") + 1);
        String requestType = "POST";
        String methodPath = getPath("Food");
        URL url = null;
        HttpURLConnection conn = null;

        try {

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String stringUserJson = gson.toJson(newFood);
            url = new URL(BASE_URL + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod(requestType);
            //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
//add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
    }

    public static boolean checkEmail(String email) {
        final String methodPath = getPath("Credentials") + "emailExist/" + email;
        URL url = null;
        String textResult = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");

//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return Boolean.parseBoolean(textResult);
    }

    public static boolean checkUsername(String username) {
        final String methodPath = getPath("Credentials") + "usernameExist/" + username;
        URL url = null;
        String textResult = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");

//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return Boolean.parseBoolean(textResult);
    }

}
