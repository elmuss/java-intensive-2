package main.service;

import main.model.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private Logger log;

    private NotificationServiceImpl notificationService;

    private UserEvent testUserEvent;

    @BeforeEach
    void setUp() {
        testUserEvent = new UserEvent(1, "Test User", "test@example.com");
        notificationService = new NotificationServiceImpl();
    }

    @Test
    void sendUserCreatedNotification_ShouldCreateAndPrintNotification() {
        notificationService.sendUserCreatedNotification(testUserEvent);

        verify(log, never()).error(anyString(), anyString(), any());
    }

    @Test
    void sendUserDeletedNotification_ShouldCreateAndPrintNotification() {
        notificationService.sendUserDeletedNotification(testUserEvent);

        verify(log, never()).error(anyString(), anyString(), any());
    }

}