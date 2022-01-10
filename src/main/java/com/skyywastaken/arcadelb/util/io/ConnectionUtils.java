package com.skyywastaken.arcadelb.util.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionUtils {
    private static URLConnection getURLConnectionFromString(String address) {
        URL connectionURL = getURL(address);
        if (connectionURL == null) {
            return null;
        }
        return getURLConnectionFromURL(connectionURL);
    }

    static URLConnection getURLConnectionFromURL(URL passedURL) {
        try {
            return passedURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static HttpURLConnection getHttpURLConnectionFromString(String connectionAddress) {
        URL connectionURL = getURL(connectionAddress);
        if (connectionURL == null) {
            return null;
        }
        return getHttpURLConnectionFromURL(connectionURL);
    }

    private static HttpURLConnection getHttpURLConnectionFromURL(URL keyCheckURL) {
        if (keyCheckURL == null) {
            return null;
        }
        try {
            return (HttpURLConnection) keyCheckURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static URL getURL(String key) {
        try {
            return new URL(key);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static InputStreamReader getInputStreamReaderFromURLConnection(URLConnection passedConnection) {
        try {
            return new InputStreamReader(passedConnection.getInputStream());
        } catch (IOException e) {
            if (passedConnection instanceof HttpURLConnection) {
                return new InputStreamReader(((HttpURLConnection) passedConnection).getErrorStream());
            }
        }
        return null;
    }


    public static JsonObject getJsonObjectFromConnection(HttpURLConnection passedConnection) {
        JsonElement keyCheckElement = JsonUtils.getJsonElementFromConnection(passedConnection);
        if (keyCheckElement == null || !keyCheckElement.isJsonObject()) {
            return null;
        }
        return keyCheckElement.getAsJsonObject();
    }
}
