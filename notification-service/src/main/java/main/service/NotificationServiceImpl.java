package main.service;

import main.model.Notification;
import main.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public NotificationServiceImpl() {

    }

    @Override
    public void sendUserCreatedNotification(UserEvent event) {
        try {
            Notification notification = new Notification();
            notification.setEmail(event.userEmail());
            notification.setMessage("Успешное создание пользователя с id=" + event.userId());
            System.out.println(notification.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления о создании пользователя для email: {}",
                    event.userEmail(), e);
        }
    }

    @Override
    public void sendUserDeletedNotification(UserEvent event) {
        try {
            Notification notification = new Notification();
            notification.setEmail(event.userEmail());
            notification.setMessage("Удаление пользователя с id=" + event.userId());
            System.out.println(notification.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления об удалении пользователя для email: {}",
                    event.userEmail(), e);
        }
    }

}
