package com.thehasnibrothers.owoj.Helper;

/*import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;*/

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 19/4/2015.
 */
public class HttpRequest {
    /*private String address;
    private HttpClient client;
    private HttpGet httpGet;

    public HttpRequest(String url)
    {
        address = url;
        client = new DefaultHttpClient();
        httpGet = new HttpGet(address);
        httpGet.addHeader("Authorization", "Basic dGVzdDp0ZXN0");
    }

    public HttpResponse getRequest()
    {
        try {
            return client.execute(httpGet);
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return null;
    }*/

    private URL url;
    private HttpURLConnection connection;

    public HttpRequest(String address)
    {
        try {
            url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(10000 /* milliseconds */);
            connection.setRequestProperty ("Authorization", "Basic dGVzdDp0ZXN0");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    public HttpURLConnection getRequest()
    {
        try {
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return connection;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpURLConnection postRequest()
    {
        try {
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            return connection;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpURLConnection putRequest()
    {
        try {
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", "application/json");
            connection.connect();
            return connection;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
