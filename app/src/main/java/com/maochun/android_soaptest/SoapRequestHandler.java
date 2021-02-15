package com.maochun.android_soaptest;

import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SoapRequestHandler {

    private Integer mRequestTimeout = 5000;
    private boolean mLogSvrRequestDetails = true;
    private String mTAG = "SoapRequestHandler";

    public Pair<Boolean, String> processPostRequest(String svrUrl, String host, String soapAction, String param) {

        Boolean result = false;
        String response = "";

        HttpURLConnection connection = null;

        try {

            URL url = new URL(svrUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);

            connection.setConnectTimeout(mRequestTimeout);
            //connection.setReadTimeout(mRequestTimeout);
            //connection.setRequestProperty("POST", "HTTP/1.1");
            //connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(param.length()));
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("Host", host);
            connection.setRequestProperty("SOAPAction", soapAction);
            //connection.setRequestProperty("Connection", "keep-alive");
            connection.connect();



            /*
            OutputStream output = connection.getOutputStream();
            byte[] b = param.getBytes("utf-8");
            output.write(b, 0, b.length);
            output.flush();
            output.close();

            */

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(out, "UTF-8"));
            writer.write(param);
            writer.close();
            out.close();

            Integer responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                response = getResponse(in);
                result = true;

                if (mLogSvrRequestDetails) {
                    Log.i(mTAG, response);

                    System.out.println(response);
                }
                in.close();
            }else{
                InputStream errIn = connection.getErrorStream();
                response = getResponse(errIn);
                //String errormsg = connection.getResponseMessage();

                Log.e(mTAG, "Request failed " + svrUrl + " " + param);
                Log.e(mTAG, "Server request response code = " + response);
                errIn.close();
            }

        } catch (Exception e) {
            response = e.getMessage();
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new Pair<>(result, response);
    }


    private String getResponse(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return builder.toString();
    }
}
