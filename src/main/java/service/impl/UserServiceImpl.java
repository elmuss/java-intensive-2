package service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import model.User;
import service.dao.UserDao;
import service.dao.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao usersDao = new UserDao();
    private final Scanner scanner = new Scanner(System.in);


    private static final String USER_NAME_EMPTY_MSG = "Имя не может быть пустым. Попробуйте ещё раз.";
    private static final String USER_NAME_DIGITS_MSG = "Имя не должно состоять только из цифр. Попробуйте ещё раз.";
    private static final String AGE_WRONG_RANGE_MSG = "Возраст должен быть от 1 до 120. Попробуйте ещё раз.";
    private static final String AGE_WRONG_FORMAT_MSG = "Возраст должен быть целым числом. Попробуйте ещё раз.";
    private static final String EMAIL_EMPTY_MSG = "Email не может быть пустым. Попробуйте ещё раз.";
    private static final String EMAIL_WRONG_FORMAT_MSG = "Некорректный формат email. Попробуйте ещё раз.";

    @Override
    @Transactional
    public void createUser() {
        log.info("Начало процесса создания нового пользователя");
        String name = readValidInput("Имя: ", this::isValidName, USER_NAME_EMPTY_MSG, USER_NAME_DIGITS_MSG);
        int age = readIntInput("Возраст: ", 1, 120, AGE_WRONG_RANGE_MSG, AGE_WRONG_FORMAT_MSG);
        String email = readValidInput("Email: ", this::isValidEmail, EMAIL_EMPTY_MSG, EMAIL_WRONG_FORMAT_MSG);

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        user.setCreatedOn(LocalDateTime.now());

        usersDao.create(user);
        System.out.println("Пользователь успешно создан!");
        log.info("Пользователь успешно создан. ID: {}", user.getId());
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Запрос на получение списка всех пользователей");
        List<User> users = usersDao.findAll();
        log.debug("Получено {} пользователей из БД", users.size());
        return users;
    }

    @Override
    public User findUser(int id) {
        log.debug("Поиск пользователя по ID: {}", id);
        User user = usersDao.findById(id);
        if (user != null) {
            log.debug("Пользователь найден: ID={}, Имя='{}'", id, user.getName());
        } else {
            log.warn("Пользователь с ID={} не найден", id);
        }
        return user;
    }

    @Override
    @Transactional
    public void updateUser(int id) {
        log.info("Начало обновления пользователя с ID: {}", id);
        User user = usersDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            log.warn("Не удалось обновить: пользователь с ID={} не существует", id);
            return;
        }

        System.out.printf("Текущее имя: %s%n", user.getName());
        String newName = readOptionalInput("Новое имя", this::isValidName,
                USER_NAME_EMPTY_MSG, USER_NAME_DIGITS_MSG);
        if (!newName.isEmpty()) {
            user.setName(newName);
            log.debug("Обновлено имя пользователя на: {}", newName);
        }

        System.out.printf("Текущий email: %s%n", user.getEmail());
        String newEmail = readOptionalInput("Новый email", this::isValidEmail,
                EMAIL_EMPTY_MSG, EMAIL_WRONG_FORMAT_MSG);
        if (!newEmail.isEmpty()) {
            user.setEmail(newEmail);
            log.debug("Обновлён email пользователя на: {}", newEmail);
        }

        System.out.printf("Текущий возраст: %d%n", user.getAge());
        Integer newAge = readOptionalIntInput("Новый возраст", 1, 120,
                AGE_WRONG_RANGE_MSG, AGE_WRONG_FORMAT_MSG);
        if (newAge != null) {
            user.setAge(newAge);
            log.debug("Обновлён возраст пользователя на: {}", newAge);
        }

        usersDao.update(user);
        System.out.println("Пользователь обновлён.");
        log.info("Пользователь с ID={} успешно обновлён", id);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        log.info("Начало удаления пользователя с ID: {}", id);
        User user = usersDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            log.warn("Не удалось удалить: пользователь с ID={} не существует", id);
            return;
        }

        usersDao.delete(user);
        System.out.println("Пользователь удалён.");
        log.info("Пользователь с ID={} успешно удалён", id);
    }

    private String readValidInput(String message, Predicate<String> validator, String... errorMessages) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (validator.test(input)) {
                return input;
            }
            for (String error : errorMessages) {
                System.out.println(error);
            }
        }
    }

    private String readOptionalInput(String message, Predicate<String> validator, String... errorMessages) {
        System.out.print(message + " (оставить пустым для пропуска): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return "";
        return readValidInput(message, validator, errorMessages);
    }

    private int readIntInput(String message, int min, int max, String rangeError, String formatError) {
        while (true) {
            System.out.print(message + ": ");
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println(rangeError);
            } catch (NumberFormatException e) {
                System.out.println(formatError);
            }
        }
    }

    private Integer readOptionalIntInput(String message, int min, int max, String rangeError, String formatError) {
        System.out.print(message + " (оставить пустым для пропуска): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;

        try {
            int value = Integer.parseInt(input);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(rangeError);
        } catch (NumberFormatException e) {
            System.out.println(formatError);
        }
        return null;
    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && !name.matches("\\d+");
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}