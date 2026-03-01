package main.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные пользователя")
public record UserDto(
        @Schema(
                description = "Уникальный идентификатор пользователя",
                example = "1"
        )
        Integer id,
        @Schema(
                description = "Имя пользователя",
                example = "Петр Петров"
        )
        String name,
        @Schema(
                description = "Email пользователя",
                example = "petr.petrov@example.com",
                format = "email"
        )
        String email,
        @Schema(
                description = "Возраст пользователя в годах",
                example = "30"
        )
        Integer age) {
}
