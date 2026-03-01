package main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные для создания нового пользователя")
public class NewUserDto {
    @NonNull
    @Schema(
            description = "Имя пользователя",
            example = "Петр Петров"
    )
    private String name;
    @NonNull
    @Schema(
            description = "Email пользователя",
            example = "petr.petrov@example.com",
            format = "email"
    )
    private String email;
    @NonNull
    @Schema(
            description = "Возраст пользователя в годах",
            example = "25"
    )
    private Integer age;
}
