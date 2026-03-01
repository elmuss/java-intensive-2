package main.service;

import main.model.UserEvent;

public interface NotificationService {
    public void sendUserCreatedNotification(UserEvent event);

    public void sendUserDeletedNotification(UserEvent event);

}
