package main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private Integer age;
}
