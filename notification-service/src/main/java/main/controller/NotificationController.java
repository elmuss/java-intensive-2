package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Notification Service", description = "API для отправки уведомлений")
public class NotificationController {

    private final NotificationService emailService;

    public NotificationController(NotificationService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/user/created")
    @Operation(summary = "Отправить уведомление о создании пользователя")
    public ResponseEntity<Map<String, String>> sendUserCreatedNotification(
            @Parameter(description = "Событие создания пользователя")
            @RequestBody UserEvent userEvent) {

        emailService.sendUserCreatedNotification(userEvent);

        return ResponseEntity.accepted()
                .body(Map.of(
                        "message", "Уведомление о создании пользователя отправлено",
                        "userId", userEvent.userId().toString()
                ));
    }

    @PostMapping("/user/deleted")
    @Operation(summary = "Отправить уведомление об удалении пользователя")
    public ResponseEntity<Map<String, String>> sendUserDeletedNotification(
            @Parameter(description = "Событие удаления пользователя")
            @RequestBody UserEvent userEvent) {

        emailService.sendUserDeletedNotification(userEvent);

        return ResponseEntity.accepted()
                .body(Map.of(
                        "message", "Уведомление об удалении пользователя отправлено",
                        "userId", userEvent.userId().toString()
                ));
    }
}
