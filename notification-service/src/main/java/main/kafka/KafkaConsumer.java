package main.kafka;

import lombok.Getter;
import lombok.Setter;
import main.model.UserEvent;
import main.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class KafkaConsumer {

    private final NotificationService notificationService;

    public KafkaConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${app.kafka.topics.user-created}")
    public void handleUserCreated(UserEvent event) {
        notificationService.sendUserCreatedNotification(event);
    }

    @KafkaListener(topics = "${app.kafka.topics.user-deleted}")
    public void handleUserDeleted(UserEvent event) {
        notificationService.sendUserDeletedNotification(event);
    }
}
