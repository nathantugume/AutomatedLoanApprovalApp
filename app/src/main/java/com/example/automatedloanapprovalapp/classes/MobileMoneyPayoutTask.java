package com.example.automatedloanapprovalapp.classes;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MobileMoneyPayoutTask {
    private static final String API_URL = "https://www.easypay.co.ug/api/";
    private static final String CLIENT_ID = "720bd5a4d9e94aff";
    private static final String CLIENT_SECRET = "4e24c1bc3ef29542";
    private final String phone;
    private final int amount;

    public interface MobileMoneyPayoutListener {
        void onPayoutSuccess(Object jsonResponse);
        void onPayoutFailure();
    }

    public MobileMoneyPayoutTask(String phone, int amount) {
        this.phone = formatPhoneNumber(phone);
        this.amount = amount;
    }

    private String formatPhoneNumber(String rawPhoneNumber) {
        if (rawPhoneNumber != null && !rawPhoneNumber.isEmpty()) {
            return rawPhoneNumber.replaceFirst("^0", "256");
        } else {
            return "";
        }
    }

    public void executePayout(MobileMoneyPayoutListener listener) {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                String reference = generateUniqueReference();

                Map<String, Object> payloadMap = new HashMap<>();
                payloadMap.put("username", CLIENT_ID);
                payloadMap.put("password", CLIENT_SECRET);
                payloadMap.put("action", "mmpayout");
                payloadMap.put("amount", amount);
                payloadMap.put("phone", phone);
                payloadMap.put("reference",reference);

                String payload = new Gson().toJson(payloadMap);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }

                        Gson gson = new Gson();
                        Object jsonResponse = gson.fromJson(response.toString(), Object.class);

                        new Handler(Looper.getMainLooper()).post(() -> listener.onPayoutSuccess(jsonResponse));
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(listener::onPayoutFailure);
                }
            } catch (Exception e) {
                Log.e("MobileMoneyPayoutTask", "Error: " + e.getMessage());
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(listener::onPayoutFailure);
            }
        }, getExecutor());
    }

    private Executor getExecutor() {
        return Executors.newFixedThreadPool(5);
    }
    // Helper method to generate a unique reference
    private String generateUniqueReference() {
        // Use a combination of timestamp and UUID to create a unique reference
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString();
        return timestamp + "_" + uuid;
    }
}
