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
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Email
    @Size(min = 6, max = 254)
    private String email;
    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 250)
    private String name;

}
