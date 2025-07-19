package com.baerchen.central.authentication.runtime.control;


import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
public interface Parser {


    default Set<String> parseSet(String input) {
        if (input == null || input.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    default <T> String parse(Set<T> input, Function<T, String> stringMapper) {
        if (input == null || input.isEmpty()) {
            return Strings.EMPTY;
        }
        return input.stream()
                .map(stringMapper)
                .collect(Collectors.joining(","));
    }

}
