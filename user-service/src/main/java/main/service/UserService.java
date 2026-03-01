package main.service;

import main.dto.NewUserDto;
import main.dto.UpdateUserDto;
import main.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserDto newUserDto);

    UserDto findUser(int id);

    void deleteUser(int id);

    UserDto updateUser(Integer id, UpdateUserDto dto);

    List<UserDto> findAllUsers();
}
