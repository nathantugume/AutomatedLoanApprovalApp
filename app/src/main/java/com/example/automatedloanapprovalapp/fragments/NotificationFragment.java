package com.example.automatedloanapprovalapp.fragments;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.example.automatedloanapprovalapp.R;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private int notificationCount;
    ArrayList<String> notificationData = new ArrayList<>();
    private PopupWindow notificationPopup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_item_notification, container, false);
        TextView notificationCountTextView = rootView.findViewById(R.id.notification_count);
        ImageView notification = rootView.findViewById(R.id.icon);
        notificationData.add("New SMS 1");
        notificationData.add("New SMS 2");
        notificationCount = notificationData.size();

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificationPopup(view);
            }
        });

        // Update the text with the current notification count
        notificationCountTextView.setText(String.valueOf(notificationCount));

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

        // Create the PopupWindow and show it below the view
        notificationPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        notificationPopup.showAsDropDown(view);
    }

}
