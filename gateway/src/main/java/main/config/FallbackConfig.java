package main.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackConfig {

    @GetMapping("/fallback/user")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "User service is temporarily unavailable");
        response.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/fallback/notification")
    public ResponseEntity<Map<String, Object>> notificationServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Notification service is temporarily unavailable");
        response.put("code", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
