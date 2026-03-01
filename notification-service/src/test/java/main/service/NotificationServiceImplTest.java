package main.service;

import main.model.Notification;
import main.model.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private Logger log;

    private NotificationServiceImpl notificationService;

    private UserEvent testUserEvent;

    @BeforeEach
    void setUp() {
        testUserEvent = new UserEvent(1, "Test User", "test@example.com");
        notificationService = new NotificationServiceImpl(log);
    }

    @Test
    void sendUserCreatedNotification_ShouldCreateAndPrintNotification() {
        notificationService.sendUserCreatedNotification(testUserEvent);

        verify(log, never()).error(anyString(), anyString(), any());
    }

    @Test
    void sendUserCreatedNotification_WhenExceptionOccurs_ShouldLogError() {
        try (MockedConstruction<Notification> mockedConstruction = Mockito.mockConstruction(Notification.class,
                (mock, context) -> {
                    doThrow(new RuntimeException("Test exception"))
                            .when(mock).setEmail(anyString());
                })) {

            notificationService.sendUserCreatedNotification(testUserEvent);

            verify(log).error(
                    eq("Ошибка при отправке уведомления о создании пользователя для email: {}"),
                    eq(testUserEvent.userEmail()),
                    any(RuntimeException.class)
            );
        }
    }

    @Test
    void sendUserDeletedNotification_ShouldCreateAndPrintNotification() {
        notificationService.sendUserDeletedNotification(testUserEvent);

        verify(log, never()).error(anyString(), anyString(), any());
    }

    @Test
    void sendUserDeletedNotification_WhenExceptionOccurs_ShouldLogError() {
        try (MockedConstruction<Notification> mockedConstruction = Mockito.mockConstruction(Notification.class,
                (mock, context) -> {
                    doThrow(new RuntimeException("Test exception"))
                            .when(mock).setEmail(anyString());
                })) {

            notificationService.sendUserDeletedNotification(testUserEvent);

            verify(log).error(
                    eq("Ошибка при отправке уведомления об удалении пользователя для email: {}"),
                    eq(testUserEvent.userEmail()),
                    any(RuntimeException.class)
            );
        }
    }
}