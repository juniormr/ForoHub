package com.forohubjr.Forohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAuthData(
        @NotBlank
        @Email
        String mail,
        @NotBlank
        String password,
        @NotBlank
        String name
) {
}
