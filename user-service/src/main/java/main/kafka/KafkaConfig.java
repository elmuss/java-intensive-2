package main.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topics.user-created}")
    private String userCreatedTopic;

    @Value("${app.kafka.topics.user-deleted}")
    private String userDeletedTopic;

    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name(userCreatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userDeletedTopic() {
        return TopicBuilder.name(userDeletedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
