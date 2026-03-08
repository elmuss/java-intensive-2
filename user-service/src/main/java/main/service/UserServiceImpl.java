package main.service;

import jakarta.transaction.Transactional;
import main.dao.UserDao;
import main.dto.NewUserDto;
import main.dto.UpdateUserDto;
import main.dto.UserDto;
import main.model.User;
import main.model.UserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.topics.user-created}")
    private String userCreatedTopic;
    @Value("${app.kafka.topics.user-deleted}")
    private String userDeletedTopic;

    public UserServiceImpl(UserDao userDao, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userDao = userDao;
        this.kafkaTemplate = kafkaTemplate;
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

        kafkaTemplate.send(userCreatedTopic,
                new UserEvent(savedUser.getId(), savedUser.getName(), savedUser.getEmail()));

        return userToDto(savedUser);
    }

    @Override
    public UserDto findUser(int id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_MSG, id)));
        return userToDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_MSG, id)));
        userDao.deleteById(id);

        kafkaTemplate.send(userDeletedTopic,
                new UserEvent(user.getId(), user.getName(), user.getEmail()));
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer id, UpdateUserDto updateUserDto) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format(USER_NOT_FOUND_MSG, id)));

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

    UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }

}