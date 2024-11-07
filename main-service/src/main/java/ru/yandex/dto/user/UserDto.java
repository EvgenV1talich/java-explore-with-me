package ru.yandex.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @NotNull
    @NotEmpty
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;

}
