package com.forohubjr.Forohub.domain.topic;

import jakarta.validation.constraints.NotBlank;

public record UpdateTopicData(
        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotBlank
        String status,
        @NotBlank
        String course
) {
}
