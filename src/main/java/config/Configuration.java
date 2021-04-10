package config;

import constant.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {
    private final Map<String, String> cookies;
    private final int amountPagesToParse;
    private final Map<String, String> categoryAndUrl;
    private static Configuration instance = null;
    private final Properties property;

    private Configuration() {
        this.property = initProperty();
        this.cookies = initCookies();
        this.amountPagesToParse = initAmountPagesToParse();
        this.categoryAndUrl = initCategoryAndUrl();
    }

    private Properties initProperty() {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("app.properties");
            property.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    private Map<String, String> initCookies() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("gdpr_permission_given", property.getProperty("gdpr_permission_given"));
        cookies.put("_cmuid", property.getProperty("_cmuid"));
        cookies.put("datadome", property.getProperty("datadome"));
        return cookies;
    }

    private int initAmountPagesToParse() {
        return Integer.parseInt(property.getProperty("pages"));
    }

    private Map<String, String> initCategoryAndUrl() {
        Map<String, String> categoryAndUrl = new HashMap<>();
        String el = property.getProperty("CATEGORY_ELECTRIC");
        String gard = property.getProperty("CATEGORY_GARDEN");
        String kid = property.getProperty("CATEGORY_KIDS");

        if (el != null) {
            categoryAndUrl.put(Constants.CATEGORY_ELECTRIC, el);
        }
        if (gard != null) {
            categoryAndUrl.put(Constants.CATEGORY_GARDEN, gard);
        }
        if (kid != null) {
            categoryAndUrl.put(Constants.CATEGORY_KIDS, kid);
        }
        return categoryAndUrl;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public int getAmountPagesToParse() {
        return amountPagesToParse;
    }

    public Map<String, String> getCategoryAndUrl() {
        return categoryAndUrl;
    }

    public static Configuration getConfiguration() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
