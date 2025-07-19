package com.baerchen.central.authentication.runtime.control;

import java.util.Set;
import java.util.stream.Collectors;

public interface Cleanable<T> {
    T cleaned();

    default Set<String> cleanSet(Set<String> input) {
        if (input == null) return Set.of();
        return input.stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }
}
