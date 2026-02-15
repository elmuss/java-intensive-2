import lombok.extern.slf4j.Slf4j;
import model.User;
import service.dao.UserService;
import service.impl.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class Main {
    private static final UserService userService = new UserServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        log.info("Запуск приложения User CRUD Application");
        System.out.println("=== User CRUD Application ===");

        while (true) {
            printMenu();
            int choice = readIntInput("Введите команду (0–5): ", 0, 5);

            switch (choice) {
                case 1 -> {
                    log.info("Пользователь выбрал создание нового пользователя");
                    userService.createUser();
                }
                case 2 -> {
                    log.info("Пользователь запросил список всех пользователей");
                    showAllUsers();
                }
                case 3 -> {
                    log.info("Пользователь запросил поиск пользователя по ID");
                    findUserById();
                }
                case 4 -> {
                    log.info("Пользователь выбрал обновление пользователя");
                    updateUserById();
                }
                case 5 -> {
                    log.info("Пользователь выбрал удаление пользователя");
                    deleteUserById();
                }
                case 0 -> {
                    System.out.println("До свидания!");
                    log.info("Приложение завершено по запросу пользователя");
                    return;
                }
                default -> {
                    System.out.println("Неверная команда. Попробуйте снова.");
                    log.warn("Введена недопустимая команда: {}", choice);
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nДоступные команды:");
        System.out.println("  1 – Создать пользователя");
        System.out.println("  2 – Просмотреть всех пользователей");
        System.out.println("  3 – Найти пользователя по ID");
        System.out.println("  4 – Обновить пользователя");
        System.out.println("  5 – Удалить пользователя");
        System.out.println("  0 – Выход");
    }

    private static int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    log.debug("Введена команда: {}", value);
                    return value;
                }
                System.out.printf("Команда должна быть от %d до %d.%n", min, max);
                log.warn("Введено недопустимое значение команды: {}", value);
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
                log.warn("Ошибка ввода: не число", e);
            }
        }
    }

    private static void showAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей в базе.");
            log.info("Список пользователей пуст");
        } else {
            System.out.println("\nСписок пользователей:");
            for (User user : users) {
                System.out.printf(
                        "ID: %d, Имя: %s, Email: %s, Возраст: %d%n",
                        user.getId(), user.getName(), user.getEmail(), user.getAge()
                );
            }
            log.info("Выведен список из {} пользователей", users.size());
        }
    }

    private static void findUserById() {
        int id = readIntInput("Введите ID пользователя: ", 1, Integer.MAX_VALUE);
        User user = userService.findUser(id);
        if (user != null) {
            System.out.printf(
                    "ID: %d, Имя: %s, Email: %s, Возраст: %d%n",
                    user.getId(), user.getName(), user.getEmail(), user.getAge()
            );
            log.info("Найден пользователь с ID={}", id);
        } else {
            System.out.println("Пользователь не найден.");
            log.warn("Пользователь с ID={} не найден", id);
        }
    }

    private static void updateUserById() {
        int id = readIntInput("Введите ID пользователя для обновления: ", 1, Integer.MAX_VALUE);
        userService.updateUser(id);
    }

    private static void deleteUserById() {
        int id = readIntInput("Введите ID пользователя для удаления: ", 1, Integer.MAX_VALUE);
        userService.deleteUser(id);
    }
}