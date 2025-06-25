package com.rishi.config;


import java.util.Base64;

public class JWT_CONSTANT {
    // Must be 64+ bytes long (for HS512)
    public static final String RAW_SECRET = "rishiSinghRanjeetSinghRishiSinghRanjeetSinghRishiSinghRanjeetSingh123456";
    public static final String SECRET_KEY = Base64.getEncoder().encodeToString(RAW_SECRET.getBytes());
    public static final String JWT_HEADER = "Authorization";
}

