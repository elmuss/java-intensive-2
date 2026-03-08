package main.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationServiceSpringdocConfig {
    @Bean
    public GroupedOpenApi notificationServiceApi() {
        return GroupedOpenApi.builder()
                .group("notification-service")
                .pathsToMatch("/notifications/**")
                .build();
    }
}
