package org.example.notify;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationManager {

    private final List<Notifier> notifiers;

    // Collection injection — Spring injects all Notifier beans, ordered by @Order
    public NotificationManager(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void notifyAll(String message) {
        for (Notifier n : notifiers) {
            n.send(message);
        }
    }
}