package com.sebamora.simulators.gpstrackersimulator;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by sebam on 11/18/2016.
 */

public class WebConnection {
    static CookieManager CookieManager;
    public WebConnection() {
        if (CookieManager == null) {
            CookieManager = new CookieManager();
        }
    }
    public String GetJsonItems(String urlRequested, String params) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlRequested);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setRequestProperty("Cookie","__test=f9df5b000c009152e6b96fe4fbd7ca1a; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");

            byte[] outputInBytes = params.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputInBytes);
            os.close();

            conn.connect();

            int status = conn.getResponseCode();

            switch (status) {
                case HttpURLConnection.HTTP_OK:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public void StoreCookies(HttpURLConnection conn) {
        //Get the cookies
        try {
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get("Set-Cookie");
            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    CookieManager.getCookieStore().add(conn.getURL().toURI(), HttpCookie.parse(cookie).get(0));
                }
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetCookies(HttpURLConnection conn) {
        if (CookieManager.getCookieStore().getCookies().size() > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            String cookies = TextUtils.join(";", CookieManager.getCookieStore().getCookies());
            conn.setRequestProperty("Cookie", cookies);
        }
    }
}


