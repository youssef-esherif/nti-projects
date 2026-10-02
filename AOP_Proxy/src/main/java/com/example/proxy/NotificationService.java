package com.example.proxy;

public interface NotificationService {
    void sendEmail(String to, String message);
    void sendSms(String to, String message);
}
