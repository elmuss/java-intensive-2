package main.controller;

import main.dto.NewUserDto;
import main.dto.UpdateUserDto;
import main.dto.UserDto;
import main.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockitoBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createUser() throws Exception {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setName("New user");
        newUserDto.setEmail("newuser@example.com");
        newUserDto.setAge(30);

        UserDto expectedUserDto = new UserDto(1, "New user", "newuser@example.com", 30);

        when(userService.createUser(any(NewUserDto.class))).thenReturn(expectedUserDto);

        ResultActions result = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New user\",\"email\":\"newuser@example.com\",\"age\":30}"));


        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New user"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.age").value(30));

        verify(userService, times(1)).createUser(any(NewUserDto.class));
    }

    @Test
    void findUser() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto(userId, "Find user", "finduser@example.com", 25);

        when(userService.findUser(userId)).thenReturn(userDto);

        ResultActions result = mvc.perform(get("/users/{id}", userId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Find user"))
                .andExpect(jsonPath("$.email").value("finduser@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService, times(1)).findUser(userId);
    }

    @Test
    void deleteUser() throws Exception {
        int userId = 1;

        doNothing().when(userService).deleteUser(userId);

        ResultActions result = mvc.perform(delete("/users/{id}", userId));

        result.andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void updateUser() throws Exception {
        int userId = 1;
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Updated Name");
        updateDto.setAge(35);
        updateDto.setEmail("updated@example.com");

        UserDto updatedUserDto = new UserDto(userId, "Updated Name", "updated@example.com", 35);

        when(userService.updateUser(eq(userId), any(UpdateUserDto.class))).thenReturn(updatedUserDto);

        ResultActions result = mvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\",\"age\":35,\"email\":\"updated@example.com\"}"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.age").value(35));

        verify(userService, times(1)).updateUser(eq(userId), any(UpdateUserDto.class));
    }

    @Test
    void findAllUsers() throws Exception {
        UserDto user1 = new UserDto(1, "User One", "user1@example.com", 28);

        UserDto user2 = new UserDto(2, "User Two", "user2@example.com", 32);

        List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.findAllUsers()).thenReturn(users);

        ResultActions result = mvc.perform(get("/users"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("User One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("User Two"));

        verify(userService, times(1)).findAllUsers();
    }
}