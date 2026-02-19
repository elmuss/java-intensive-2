package main.dto;

import lombok.Data;

@Data
public class NewUserDto {
    private String name;
    private String email;
    private Integer age;
}
