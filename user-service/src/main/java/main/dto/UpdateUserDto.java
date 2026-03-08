package main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные для обновления пользователя. Указать поля, которые требуется изменить.")
public class UpdateUserDto {
    @Schema(
            description = "Новое имя пользователя (опционально)",
            example = "Илья Ильин",
            allowableValues = {"может быть null — поле не изменится"}
    )
    private String name;
    @Schema(
            description = "Новый email пользователя (опционально)",
            example = "ilia.ilin@example.com",
            format = "email",
            allowableValues = {"может быть null — поле не изменится"}
    )
    private String email;
    @Schema(
            description = "Новый возраст пользователя (опционально)",
            example = "30",
            allowableValues = {"может быть null — поле не изменится"}
    )
    private Integer age;
}
