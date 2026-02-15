package service.impl;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.dao.UserDao;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private void setInput(String input) {
        Scanner testScanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        userService = new UserServiceImpl(userDao, testScanner);
    }

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao, new Scanner(System.in));
    }

    @Test
    @DisplayName("Создание пользователя прошло успешно")
    void testCreateUserIsSuccessful() {
        setInput("Name\n40\nname@example.com");

        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("Name", capturedUser.getName());
        assertEquals(40, capturedUser.getAge());
        assertEquals("name@example.com", capturedUser.getEmail());
        assertNotNull(capturedUser.getCreatedOn());
    }

    @Test
    @DisplayName("Создание пользователя с пустым именем")
    void testCreateUserWithEmptyName() {
        setInput("\nName\n30\nname@example.com");

        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());

        assertEquals("Name", userCaptor.getValue().getName());
    }

    @Test
    @DisplayName("Создание пользователя с именем из числового значения")
    void testCreateUserWithNameIsNumber() {
        setInput("123\nName\n30\nname@example.com");

        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());
        assertEquals("Name", userCaptor.getValue().getName());
    }

    @Test
    @DisplayName("Создание пользователя с возрастом не из числового значения")
    void testCreateUserWithAgeNotNumber() {
        setInput("Name\nabc\n40\nname@example.com");
        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());
        assertEquals(40, userCaptor.getValue().getAge());
    }

    @Test
    @DisplayName("Создание пользователя с возрастом больше предельного")
    void testCreateUserAgeOverRangeLimit() {
        setInput("Name\n150\n25\n name@example.com");
        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());
        assertEquals(25, userCaptor.getValue().getAge());
    }

    @Test
    @DisplayName("Создание пользователя с пустой электронной почтой")
    void testCreateUserWithEmptyEmail() {
        setInput("Name\n35\n\nname@example.com");
        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());
        assertEquals("name@example.com", userCaptor.getValue().getEmail());
    }

    @Test
    @DisplayName("Создание пользователя с электронной почтой в неверном формате")
    void testCreateUserWithEmailWrongFormat() {
        setInput("Name\n40\nname\nname@example.com");
        userService.createUser();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(userCaptor.capture());
        assertEquals("name@example.com", userCaptor.getValue().getEmail());
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void testFindAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userDao.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAllUsers();
        assertEquals(2, result.size());
        verify(userDao).findAll();
    }

    @Test
    @DisplayName("Получение пользователя прошло успешно")
    void testFindUserFound() {
        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setName("FoundUser");
        when(userDao.findById(1)).thenReturn(expectedUser);

        User result = userService.findUser(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("FoundUser", result.getName());
    }

    @Test
    @DisplayName("Пользователь не найден")
    void testFindUserNotFound() {
        when(userDao.findById(999)).thenReturn(null);
        User result = userService.findUser(999);
        assertNull(result);
        verify(userDao).findById(999);
    }

    @Test
    @DisplayName("Обновление только возраста у пользователя")
    void testUpdateUserAgeOnly() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("User");
        existingUser.setEmail("email@example.com");
        existingUser.setAge(30);
        when(userDao.findById(1)).thenReturn(existingUser);

        setInput("\n\n45");
        userService.updateUser(1);

        verify(userDao).update(existingUser);
        assertEquals(45, existingUser.getAge());
    }

    @Test
    @DisplayName("Удаление пользователя прошло успешно")
    void testDeleteUserSuccessful() {
        User user = new User();
        user.setId(1);
        when(userDao.findById(1)).thenReturn(user);

        userService.deleteUser(1);

        verify(userDao).delete(user);
        verify(userDao).findById(1);
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя")
    void testDeleteUserNonExists() {
        when(userDao.findById(999)).thenReturn(null);

        userService.deleteUser(999);

        verify(userDao, never()).delete(any());
        verify(userDao).findById(999);
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя (id нет в базе данных)")
    void testDeleteUserNonExistingId() {
        when(userDao.findById(0)).thenReturn(null);

        userService.deleteUser(0);

        verify(userDao, never()).delete(any());
        verify(userDao).findById(0);
    }

    @Test
    @DisplayName("Удаление пользователя с id в некорректном формате")
    void testDeleteUserInvalidId() {
        when(userDao.findById(0)).thenReturn(null);

        userService.deleteUser(0);

        verify(userDao, never()).delete(any());
        verify(userDao).findById(0);
    }

    @Test
    @DisplayName("Удаление пользователя с отрицательным значением id")
    void testDeleteUserNegativeId() {
        when(userDao.findById(-1)).thenReturn(null);

        userService.deleteUser(-1);

        verify(userDao, never()).delete(any());
        verify(userDao).findById(-1);
    }

    @Test
    @DisplayName("Удаление пользователя с выбрасыванием исключения")
    void testDeleteUserExceptionInDao() {
        User user = new User();
        user.setId(1);
        when(userDao.findById(1)).thenReturn(user);
        doThrow(new RuntimeException("DB error")).when(userDao).delete(user);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1));
        verify(userDao).delete(user);
    }
}
