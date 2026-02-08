package service.dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.HibernateSessionFactoryUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private final UserDao userDao = new UserDao();

    @BeforeAll
    static void setUpHibernate() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
    }

    @AfterEach
    void cleanUp() {
        try (var session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Создание пользователя и чтение по id")
    void createAndFindById() {
        User user = new User();
        user.setName("Name LastName");
        user.setEmail("name@example.com");
        user.setAge(28);

        userDao.create(user);

        User found = userDao.findById(user.getId());

        assertNotNull(found);
        assertEquals("Name LastName", found.getName());
        assertEquals("name@example.com", found.getEmail());
        assertEquals(28, found.getAge());
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void findAll() {
        User u1 = new User();
        u1.setName("Name1");
        u1.setEmail("name1@example.com");
        u1.setAge(15);
        User u2 = new User();
        u2.setName("Name2");
        u2.setEmail("name2@example.com");
        u2.setAge(18);

        userDao.create(u1);
        userDao.create(u2);

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Name1")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Name2")));
    }

    @Test
    @DisplayName("Обновление пользователя")
    void update() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");
        user.setAge(50);
        userDao.create(user);

        user.setName("New Name");
        user.setEmail("new@example.com");
        user.setAge(55);
        userDao.update(user);

        User updated = userDao.findById(user.getId());

        assertEquals("New Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(55, updated.getAge());
    }

    @Test
    @DisplayName("Удаление пользователя")
    void delete() {
        User user = new User();
        user.setName("To Be Deleted");
        user.setEmail("delete@example.com");
        user.setAge(60);
        userDao.create(user);

        userDao.delete(user);

        User deleted = userDao.findById(user.getId());
        assertNull(deleted);
    }

    @Test
    @DisplayName("Поиск несуществующего пользователя (id нет в базе данных)")
    void findById_nonExistingId() {
        User notFound = userDao.findById(999);
        assertNull(notFound);
    }
}