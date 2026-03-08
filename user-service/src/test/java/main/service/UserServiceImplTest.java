package main.service;

import main.dao.UserDao;
import main.dto.NewUserDto;
import main.dto.UpdateUserDto;
import main.dto.UserDto;
import main.model.User;
import main.model.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyInt;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestPropertySource(properties = {
        "app.kafka.topics.user-created=user.created",
        "app.kafka.topics.user-deleted=user.deleted"
})
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private NewUserDto newUserDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAge(30);

        newUserDto = new NewUserDto();
        newUserDto.setName("New User");
        newUserDto.setEmail("new@example.com");
        newUserDto.setAge(25);

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Updated Name");
        updateUserDto.setAge(35);
        updateUserDto.setEmail("updated@example.com");
    }

    @Test
    void createUser_ShouldSaveUserAndSendKafkaEvent() {
        ReflectionTestUtils.setField(userService, "userCreatedTopic", "user.created");

        when(userDao.save(any(User.class))).thenReturn(testUser);
        when(kafkaTemplate.send(anyString(), any(UserEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        UserDto result = userService.createUser(newUserDto);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.id());
        assertEquals(testUser.getName(), result.name());
        assertEquals(testUser.getEmail(), result.email());
        assertEquals(testUser.getAge(), result.age());

        verify(userDao, times(1)).save(any(User.class));
        verify(kafkaTemplate, times(1))
                .send(eq("user.created"), any(UserEvent.class));
    }

    @Test
    void findUser_ShouldReturnUserDtoWhenUserExists() {
        when(userDao.findById(1)).thenReturn(Optional.of(testUser));

        UserDto result = userService.findUser(1);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.id());
        assertEquals(testUser.getName(), result.name());
        assertEquals(testUser.getEmail(), result.email());
        assertEquals(testUser.getAge(), result.age());
    }

    @Test
    void findUser_ShouldThrowExceptionWhenUserNotFound() {
        int nonExistentId = 999;
        when(userDao.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.findUser(nonExistentId));
        assertEquals("User with id=999 was not found", exception.getMessage());
    }

    @Test
    void deleteUser_ShouldDeleteUserAndSendKafkaEvent() {
        ReflectionTestUtils.setField(userService, "userDeletedTopic", "user.deleted");

        when(userDao.findById(1)).thenReturn(Optional.of(testUser));
        doNothing().when(userDao).deleteById(1);
        when(kafkaTemplate.send(anyString(), any(UserEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        userService.deleteUser(1);

        verify(userDao, times(1)).findById(1);
        verify(userDao, times(1)).deleteById(1);
        verify(kafkaTemplate, times(1))
                .send(eq("user.deleted"), any(UserEvent.class));
    }

    @Test
    void deleteUser_ShouldThrowExceptionWhenUserNotFound() {
        int nonExistentId = 999;
        when(userDao.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(nonExistentId));
        assertEquals("User with id=999 was not found", exception.getMessage());

        verify(userDao, times(1)).findById(nonExistentId);
        verify(userDao, never()).deleteById(anyInt());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void updateUser_ShouldUpdateUserAndReturnDto() {
        when(userDao.findById(1)).thenReturn(Optional.of(testUser));
        when(userDao.save(testUser)).thenReturn(testUser);

        UserDto result = userService.updateUser(1, updateUserDto);

        assertNotNull(result);
        assertEquals(updateUserDto.getName(), result.name());
        assertEquals(updateUserDto.getAge(), result.age());
        assertEquals(updateUserDto.getEmail(), result.email());

        assertEquals(updateUserDto.getName(), testUser.getName());
        assertEquals(updateUserDto.getAge(), testUser.getAge());
        assertEquals(updateUserDto.getEmail(), testUser.getEmail());

        verify(userDao, times(1)).findById(1);
        verify(userDao, times(1)).save(testUser);
    }

    @Test
    void updateUser_ShouldThrowExceptionWhenUserNotFound() {
        int nonExistentId = 999;
        when(userDao.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(nonExistentId, updateUserDto));
        assertEquals("User with id=999 was not found", exception.getMessage());

        verify(userDao, times(1)).findById(nonExistentId);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void findAllUsers_ShouldReturnAllUsersAsDtoList() {
        List<User> users = List.of(testUser);
        when(userDao.findAll()).thenReturn(users);

        List<UserDto> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        UserDto userDto = result.get(0);
        assertEquals(testUser.getId(), userDto.id());
        assertEquals(testUser.getName(), userDto.name());
        assertEquals(testUser.getEmail(), userDto.email());
        assertEquals(testUser.getAge(), userDto.age());

        verify(userDao, times(1)).findAll();
    }

    @Test
    void findAllUsers_ShouldReturnEmptyListWhenNoUsers() {
        when(userDao.findAll()).thenReturn(List.of());

        List<UserDto> result = userService.findAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findAll();
    }
}