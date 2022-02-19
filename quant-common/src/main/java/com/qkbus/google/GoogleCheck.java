package com.qkbus.google;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class GoogleCheck {


    public static void main(String[] args) {


        GoogleAuthenticator gAuth = new GoogleAuthenticator();


        System.out.println(createGoogleAuthKey());
        System.out.println(createGoogleAuthKey());
        System.out.println(createGoogleAuthKey());

        // System.out.println(gAuth.authorize("MO5KFNDSUIELYD4Y", 941747));


    }


    //验证
    public static Boolean checkGoogleAuthKey(String googleAuthKey, Integer googleAuth) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(googleAuthKey, googleAuth);
    }

    //创建
    public static String createGoogleAuthKey() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }


}
