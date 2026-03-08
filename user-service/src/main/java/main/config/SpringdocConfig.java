package main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringdocConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JAVA-INTENSIVE-2 API")
                        .version("1.0.0")
                        .description("API для управления пользователями и уведомлениями"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development server")));
    }
}
