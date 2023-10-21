package com.example.automatedloanapprovalapp.classes;
import android.annotation.SuppressLint;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MobileMoneyPayoutTask {
    private String apiUrl = "https://www.easypay.co.ug/api/";
    private String clientId = "cbdc1aeeeaa4667f";
    private String clientSecret = "0a66d21d6b7cb635";
    private String phone;
    private int amount;

    public interface MobileMoneyPayoutListener {
        void onPayoutSuccess();
        void onPayoutFailure();
    }

    public MobileMoneyPayoutTask(String phone, int amount) {
        this.phone = formatPhoneNumber(phone);
        this.amount = amount;
    }

    private String formatPhoneNumber(String rawPhoneNumber) {
        // Check for null and empty string
        if (rawPhoneNumber != null && !rawPhoneNumber.isEmpty()) {
            // Remove preceding 0 and add 256 to the phone number
            return rawPhoneNumber.replaceFirst("^0", "256");
        } else {
            // Handle null or empty input, return an empty string or throw an exception
            // For example, you can return an empty string:
            return "";

            // Or you can throw an exception:
            // throw new IllegalArgumentException("Invalid phone number");
        }
    }


    public void executePayout(MobileMoneyPayoutListener listener) {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                @SuppressLint("DefaultLocale") String payload = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"action\":\"mmpayout\",\"amount\":%d,\"phone\":\"%s\"}",
                        clientId, clientSecret, amount, phone);

                Log.d("payload",payload);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("payout","response"+responseCode);
                    listener.onPayoutSuccess();
                } else {
                    listener.onPayoutFailure();
                }
            } catch (Exception e) {
                Log.e("MobileMoneyPayoutTask", "Error: " + e.getMessage());
                e.printStackTrace();
                listener.onPayoutFailure();
            }
        }, getExecutor());
    }

    private Executor getExecutor() {
        // You can customize the executor as per your requirements.
        return Executors.newFixedThreadPool(5); // Example: Using a fixed thread pool with 5 threads
    }
}

