package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import main.dto.NewUserDto;
import main.dto.UpdateUserDto;
import main.dto.UserDto;
import main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Service", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя",
            description = "Добавление нового пользователя в систему")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Данные для создания нового пользователя")
            @RequestBody NewUserDto newUserDto) {
        UserDto createdUser = userService.createUser(newUserDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id",
            description = "Получение сохраненного в системе пользователя с укзанным id")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<UserDto> findUser(
            @Parameter(description = "id пользователя", example = "1")
            @PathVariable("id") Integer id) {
        UserDto userDto = userService.findUser(id);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя по id",
            description = "Удаление сохраненного в системе пользователя с укзанным id")
    public void deleteUser(
            @Parameter(description = "id пользователя", example = "1")
            @PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя по id",
            description = "Обновление сохраненного в системе пользователя с укзанным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден")
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "id пользователя", example = "1")
            @PathVariable("id") Integer id,
            @Parameter(
                    description = "Данные для обновления пользователя. Указать поля, которые требуется изменить.",
                    required = true
            )
            @RequestBody UpdateUserDto updateDto) {
        UserDto updatedUser = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей",
            description = "Получение списка всех сохраненных в системе пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ со списком пользователей",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
                    })
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

}
