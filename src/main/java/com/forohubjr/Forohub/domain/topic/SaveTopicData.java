package com.forohubjr.Forohub.domain.topic;

import jakarta.validation.constraints.NotBlank;

public record SaveTopicData(
        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotBlank
        String course

) {
}
