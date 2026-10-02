package com.example.proxy;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendEmail(String to, String message) {
        System.out.println("Sending email to " + to + ": " + message);
    }

    @Override
    public void sendSms(String to, String message) {
        System.out.println("Sending SMS to " + to + ": " + message);
    }
}
