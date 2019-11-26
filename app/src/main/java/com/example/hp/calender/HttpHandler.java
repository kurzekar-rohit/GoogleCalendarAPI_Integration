package com.example.hp.calender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(String s) {
    }

    public String makeServiceCall(String reqUrl) throws Exception{
        URL url =new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        //    InputStream in = new BufferedInputStream(conn.getOutputStream());
        wr.flush();
        wr.close();
//            response = convertStreamToString(in);
        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine())!= null){
            response.append(inputLine);
        }
//        JSONObject json = new JSONObject(buffer.toString());
//            String accessToken = json.getString("access_token");

        in.close();
        //  }
//        catch (MalformedURLException e) {
//            Log.e(TAG, "MalformedURLException: " + e.getMessage());
//        } catch (ProtocolException e) {
//            Log.e(TAG, "ProtocolException: " + e.getMessage());
//        } catch (IOException e) {
//            Log.e(TAG, "IOException: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
        return response.toString();
    }
}
