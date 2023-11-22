package com.example.automatedloanapprovalapp.classes;

import java.util.Map;

public class Notification {

    private String message;
    private String target;
    private String from;
    private String date;


    private Map<String, String> userStatusMap;  // Mapping from user to status


    public Notification() {
    }

    public Notification(String message, String target, String from, String date) {
        this.message = message;
        this.target = target;
        this.from = from;
        this.date = date;
    }

    public Notification(String message, String target, String from, String date, Map<String, String> userStatusMap) {
        this.message = message;
        this.target = target;
        this.from = from;
        this.date = date;
        this.userStatusMap = userStatusMap;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, String> getUserStatusMap() {
        return userStatusMap;
    }

    public void setUserStatusMap(Map<String, String> userStatusMap) {
        this.userStatusMap = userStatusMap;
    }
}
