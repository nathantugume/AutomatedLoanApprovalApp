package com.example.automatedloanapprovalapp.fragments;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.Notification;
import com.example.automatedloanapprovalapp.classes.User;
import com.example.automatedloanapprovalapp.classes.UserManager;
import com.example.automatedloanapprovalapp.ui.admin.AdminManageLoanActivity;
import com.example.automatedloanapprovalapp.ui.customer.ApplicationStatusActivity;
import com.example.automatedloanapprovalapp.ui.customer.CustomerDashboardActivity;
import com.example.automatedloanapprovalapp.ui.loanofficer.ManageApplicationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {
    private int notificationCount;
    ArrayList<String> notificationData = new ArrayList<>();
    ArrayList<String> notificationId = new ArrayList<>();
    private PopupWindow notificationPopup;

    private FirestoreCRUD firestoreCRUD = new FirestoreCRUD();

    private UserManager userManager;
    private NotificationManagerCompat notificationManager;
    private Notification notification1;

    private User user = new User();
    private String uid;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 123; // Choose any value



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_item_notification, container, false);
        TextView notificationCountTextView = rootView.findViewById(R.id.notification_count);
        ImageView notification = rootView.findViewById(R.id.icon);
        userManager = new UserManager(rootView.getContext());
         uid = userManager.getCurrentUser().getUid();
        notificationManager = NotificationManagerCompat.from(requireContext());


        firestoreCRUD.readDocument("users", uid, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    user = snapshot.toObject(User.class);
                    if (user.getRole().equals("customer")) {
                        setCustomerNotification();
                    } else {
                        setManagementNotification();
                    }
                }
            }

            private void setManagementNotification() {
                firestoreCRUD.queryDocuments("notifications", "target", "management", task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                             notification1 = snapshot.toObject(Notification.class);

                            try {
                                if (notification1.getUserStatusMap() == null || !notification1.getUserStatusMap().containsKey(uid)){
                                    notificationData.add(notification1.getMessage());
                                    notificationId.add(snapshot.getId());
                                }
                            }catch (Exception e){
                                Log.d("NotificationFragment",e.getMessage());
                            }


                        }
                        displayNotifications(notificationData);
                        notificationCountTextView.setText(String.valueOf(notificationData.size()));
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
            }

            private void setCustomerNotification() {

                firestoreCRUD.queryDocuments("notifications", "target", "customer", task -> {

                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                             notification1 = snapshot.toObject(Notification.class);
                            try {
                                if (notification1.getUserStatusMap().containsKey(uid) && notification1.getUserStatusMap().containsValue("unread") || notification1.getUserStatusMap().isEmpty()){
                                    notificationData.add(notification1.getMessage());
                                    notificationId.add(snapshot.getId());
                                }
                            }catch (Exception e){
                                Log.d("NotificationFragment", e.getMessage());
                            }


                        }
                        displayNotifications(notificationData);
                        notificationCountTextView.setText(String.valueOf(notificationData.size()));
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
            }
        });


        notification.setOnClickListener(view -> showNotificationPopup(view));

        // Update the text with the current notification count


        return rootView;
    }

    private void showNotificationPopup(View view) {
        // Use the Fragment's context to get the LayoutInflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate the custom layout for notifications
        View popupView = inflater.inflate(R.layout.popup_notifications, null);

        // Set up the ListView with sample notification data
        ListView listView = popupView.findViewById(R.id.notification_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, notificationData);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                long notificationPosition = adapterView.getItemIdAtPosition(i);
                String selectedNotificationId = notificationId.get((int) notificationPosition);
               mark_notification_read(selectedNotificationId);

            }

            private void mark_notification_read(String selectedNotificationId) {

                Map<String, String> status = new HashMap<>();
                status.put(uid, "viewed");
                notification1.setUserStatusMap(status);

                firestoreCRUD.updateDocument("notifications", selectedNotificationId, notification1, task -> {
                    if (task.isSuccessful()){
                        if (user.getRole().equals("loan_manager")){
                            startActivity(new Intent(getContext(), ManageApplicationActivity.class));
                        }else if (user.getRole().equals("admin")){
                            startActivity(new Intent(getContext(), AdminManageLoanActivity.class));
                        } else if (user.getRole().equals("customer")) {
                            startActivity(new Intent(getContext(), ApplicationStatusActivity.class));

                        }

                    }
                });

            }
        });



        // Create the PopupWindow and show it below the view
        notificationPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        notificationPopup.showAsDropDown(view);

    }

    private void displayNotifications(ArrayList<String> notifications) {
        Log.d("NotificationFragment", "displayNotifications called");


        // Check for notification permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request the missing permissions
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            return;
        }

        // Use the notifications list to create and display Android notifications
        String ACTION = "View";
        // Create a unique notification channel ID
        String channelId = "loan_app";
        createNotificationChannel(channelId);

        // Create and display individual notifications
        for (int i = 0; i < notifications.size(); i++) {
                    String notification = notifications.get(i);
            // Create an explicit intent for an Activity in your app.
            Intent intent;
            if (user.getRole().equals("loan_manager")){
               intent  = new Intent(requireContext(), ManageApplicationActivity.class);
            } else if (user.getRole().equals("admin")) {

                intent  = new Intent(requireContext(), AdminManageLoanActivity.class);
            }
            else {
                intent  = new Intent(requireContext(), ApplicationStatusActivity.class);
            }


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                    .setSmallIcon(R.mipmap.agriloan)
                    .setContentTitle("Manager Notification")
                    .setContentText(notification)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.mipmap.agriloan,ACTION,pendingIntent)
                    .setAutoCancel(true);
//             .addAction(R.drawable.ic_snooze, getString(R.string.snooze),
//                snoozePendingIntent);

            notificationManager.notify(i, builder.build());
        }
    }
    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = user.getRole();
            String description = "Messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now call the displayNotifications method again
                displayNotifications(notificationData);
            } else {
                // Permission denied, handle it accordingly (e.g., show a message)
                Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
