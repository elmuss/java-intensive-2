package main.controller;

import main.model.UserEvent;
import main.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {

    private final NotificationService emailService;

    public NotificationController(NotificationService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/user/created")
    public ResponseEntity<Map<String, String>> sendUserCreatedNotification(
            @RequestBody UserEvent userEvent) {

        emailService.sendUserCreatedNotification(userEvent);

        return ResponseEntity.accepted()
                .body(Map.of(
                        "message", "Уведомление о создании пользователя отправлено",
                        "userId", userEvent.userId().toString()
                ));
    }

    @PostMapping("/user/deleted")
    public ResponseEntity<Map<String, String>> sendUserDeletedNotification(
            @RequestBody UserEvent userEvent) {

        emailService.sendUserDeletedNotification(userEvent);

        return ResponseEntity.accepted()
                .body(Map.of(
                        "message", "Уведомление об удалении пользователя отправлено",
                        "userId", userEvent.userId().toString()
                ));
    }
}
