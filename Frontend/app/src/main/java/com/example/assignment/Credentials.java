package com.example.assignment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Credentials {
    private Users userid;
    private String username;
    private String passwordhash;
    private Date signupdate;

    Credentials(String username, String passwordhash, Date signupdate) {
        this.username = username;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
    }

    Credentials() {
    }

    public Users getUserid() {
        return this.userid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPasswordhash() {
        return this.passwordhash;
    }

    public Date getSignupdate() {
        return this.signupdate;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordhash(String password) {
        this.passwordhash = md5Hash(password);
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }

    public static String md5Hash(String text) {
        try {

            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
