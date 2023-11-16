package com.example.automatedloanapprovalapp.classes;
import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MobileMoneyDepositTask {

    private String apiUrl = "https://www.easypay.co.ug/api/";
    private String clientId = "";
    private String clientSecret = "";
    private String phone ;
    private int amount ;
    private  StringBuilder response;
    private int reference = 12367;
    private String reason;

    public interface MobileMoneyDepositListener {
        void onDepositSuccess(String s);
        void onDepositFailure();
    }

    public void depositMoney(String tid,int amount,String phoneNumber, int actualPayment, MobileMoneyDepositListener listener) {
        reason = tid;
        phone = phoneNumber;
        amount = actualPayment;

        int finalAmount = amount;
        new Thread(() -> {
            try {
                // Create the JSON payload
                @SuppressLint("DefaultLocale") String payload = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"action\":\"mmdeposit\",\"amount\":%d,\"phone\":\"%s\",\"currency\":\"UGX\",\"reference\":%d,\"reason\":\"%s\"}",
                        clientId, clientSecret, 500, phone, reference, reason);

                // Create the connection
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Write the payload to the connection output stream
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response code
                int responseCode = connection.getResponseCode();

                // Read the response
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                     response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Handle the successful response here
                    listener.onDepositSuccess(response.toString()); // Pass response to the listener
                } else {
                    // Handle the failure, e.g., show an error message
                    listener.onDepositFailure();
                }
            } catch (Exception e) {
                // Handle exceptions, e.g., network errors
                listener.onDepositFailure();
                Log.d("error","message: "+e.getMessage());
            }
        }).start();
    }
}


