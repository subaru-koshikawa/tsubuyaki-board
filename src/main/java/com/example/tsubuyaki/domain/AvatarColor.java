package com.example.tsubuyaki.domain;

import java.util.Set;

public final class AvatarColor {

    public static final String DEFAULT = "gray";

    private static final Set<String> ALLOWED = Set.of(DEFAULT, "red", "blue", "green", "yellow");

    private AvatarColor() {
    }

    public static String safe(String value) {
        if (value == null) {
            return DEFAULT;
        }
        String normalized = value.trim().toLowerCase();
        return ALLOWED.contains(normalized) ? normalized : DEFAULT;
    }
}
