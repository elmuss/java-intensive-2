package main.service;

import main.dao.UserDao;
import main.dto.NewUserDto;
import main.dto.UpdateUserDto;
import main.dto.UserDto;
import jakarta.transaction.Transactional;
import main.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    private static final String USER_NOT_FOUND_MSG = "User with id=%d was not found";

    @Override
    @Transactional
    public UserDto createUser(NewUserDto newUserDto) {
        User newUser = new User();
        newUser.setName(newUserDto.getName());
        newUser.setEmail(newUserDto.getEmail());
        newUser.setAge(newUserDto.getAge());

        User savedUser = userDao.save(newUser);
        return userToDto(savedUser);
    }

    @Override
    public UserDto findUser(int id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MSG));
        return userToDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer id, UpdateUserDto updateUserDto) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MSG));

        if (updateUserDto.getName() != null) user.setName(updateUserDto.getName());
        if (updateUserDto.getAge() != null) user.setAge(updateUserDto.getAge());
        if (updateUserDto.getEmail() != null) user.setEmail(updateUserDto.getEmail());

        User updated = userDao.save(user);
        return userToDto(updated);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userDao.findAll().stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    private UserDto userToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        return dto;
    }

}