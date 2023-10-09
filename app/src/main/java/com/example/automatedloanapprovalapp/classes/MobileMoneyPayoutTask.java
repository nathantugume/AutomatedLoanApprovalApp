package com.example.automatedloanapprovalapp.classes;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MobileMoneyPayoutTask extends AsyncTask<Void, Void, Integer> {
    private String apiUrl = "https://www.easypay.co.ug/api/";
    private String clientId = "cbdc1aeeeaa4667f";
    private String clientSecret = "0a66d21d6b7cb635";
    private String phone = "256783836394";
    private int amount = 500;

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            @SuppressLint("DefaultLocale") String payload = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"action\":\"mmpayout\",\"amount\":%d,\"phone\":\"%s\"}",
                    clientId, clientSecret, amount, phone);

            Log.d("MobileMoneyPayoutTask", "Request Payload: " + payload); // Log the request payload

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            Log.d("MobileMoneyPayoutTask", "Response Code: " + responseCode); // Log the response code

            // Return the response code to onPostExecute
            return responseCode;
        } catch (Exception e) {
            Log.e("MobileMoneyPayoutTask", "Error: " + e.getMessage()); // Log the error message
            e.printStackTrace();
            // Return an error code, e.g., -1, to indicate failure
            return -1;
        }
    }

    // This method will be called after doInBackground is finished
    @Override
    protected void onPostExecute(Integer responseCode) {
        // Handle the response code here
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Log.d("MobileMoneyPayoutTask", "Request was successful");
            // Request was successful
            // Perform further operations if needed
        } else {
            Log.e("MobileMoneyPayoutTask", "Request failed. Response Code: " + responseCode);
            // Request failed
            // Handle the failure, e.g., show an error message
        }
    }
}
