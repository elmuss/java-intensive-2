package main.model;

public record UserEvent(
        Integer userId,
        String userName,
        String userEmail
) {}
