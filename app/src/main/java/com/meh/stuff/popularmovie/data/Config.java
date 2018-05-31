package com.meh.stuff.popularmovie.data;

public class Config {

    public static final String BASE_CONFIG_KEY = "images";
    public static final String IMAGE_BASE_URL = "base_url";
    public static final String IMAGE_SECURE_BASE_URL = "secure_base_url";

    private String baseUrl;
    private String secureBaseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    public void setSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
    }
}
