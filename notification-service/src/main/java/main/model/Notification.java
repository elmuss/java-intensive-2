package main.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Notification {
    private String email;
    private String message;
}
