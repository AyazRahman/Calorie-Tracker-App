package com.example.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.fatsecret.platform.services.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class FatSecretApi {

    final static private String APP_KEY = "83b757f9203d49de9bd6a0ecf03ff08d";
    final static private String APP_SECRET = "e70ee3318c2045afb29da256d9c2ea1a";

    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    final static private String APP_SIGNATURE_METHOD = "HmacSHA1";
    final static private String HTTP_METHOD = "GET";

    private static String nonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();
        for (int i = 0; i < r.nextInt(8) + 2; i++) {
            n.append(r.nextInt(26) + 'a');
        }
        return n.toString();
    }

    private static String[] generateOauthParams() {
        String[] a = {
                "oauth_consumer_key=" + APP_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json"
        };
        return a;
    }

    private static String join(String[] params, String separator) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                b.append(separator);
            }
            b.append(params[i]);
        }
        return b.toString();
    }

    private static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String encode(String url) {
        if (url == null)
            return "";

        try {
            return URLEncoder.encode(url, "utf-8")
                    .replace("+", "%20")
                    .replace("!", "%21")
                    .replace("*", "%2A")
                    .replace("\\", "%27")
                    .replace("(", "%28")
                    .replace(")", "%29");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }

    private static String sign(String method, String uri, String[] params) throws UnsupportedEncodingException {

        String encodedURI = encode(uri);
        String encodedParams = encode(paramify(params));

        String[] p = {method, encodedURI, encodedParams};

        String text = join(p, "&");
        String key = APP_SECRET + "&";
        SecretKey sk = new SecretKeySpec(key.getBytes(), APP_SIGNATURE_METHOD);
        String sign = "";
        try {
            Mac m = Mac.getInstance(APP_SIGNATURE_METHOD);
            m.init(sk);
            sign = encode(new String(Base64.encode(m.doFinal(text.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {

        } catch (java.security.InvalidKeyException e) {

        }
        return sign;
    }

    public static Long getFoodItems(String query) throws UnsupportedEncodingException {
        String result = "";

        List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("max_results=1");
        params.add("search_expression=" + encode(query));
        params.add("oauth_signature=" + sign(HTTP_METHOD, APP_URL, params.toArray(template)));
        long id = 0;

        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            URLConnection api = url.openConnection();
            String line;
            //StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));

            while ((line = reader.readLine()) != null) result += line;

            JSONObject jsonObject = new JSONObject(result);
            id = jsonObject.getJSONObject("foods").getJSONObject("food").getLong("food_id");

        } catch (Exception e) {
            JSONObject error = new JSONObject();
            // error.put("message", "There was an error in processing your request. Please try again later.");
            // result.put("error", error);
        }

        return id;
    }

    public static HashMap<String, String> getFoodItem(long id) throws UnsupportedEncodingException {
        String result = "";

        List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add("method=food.get");
        params.add("food_id=" + encode("" + id));
        params.add("oauth_signature=" + sign(HTTP_METHOD, APP_URL, params.toArray(template)));

        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            URLConnection api = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));

            while ((line = reader.readLine()) != null) result += line;


        } catch (Exception e) {

        }

        return extractInfo(result);
    }

    private static HashMap<String, String> extractInfo(String result) {
        HashMap<String, String> info = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("food");
            info.put("name", jsonObject.getString("food_name"));
            jsonObject = jsonObject.getJSONObject("servings").getJSONArray("serving").getJSONObject(0);
            info.put("calories", jsonObject.getString("calories"));
            info.put("fat", jsonObject.getString("fat"));
            info.put("serving unit", jsonObject.getString("metric_serving_unit"));
            info.put("serving amount", jsonObject.getString("metric_serving_amount"));


        } catch (Exception e) {
        }
        return info;
    }

}
