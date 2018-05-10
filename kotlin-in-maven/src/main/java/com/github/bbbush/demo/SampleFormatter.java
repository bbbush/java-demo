package com.github.bbbush.demo;

import java.util.Optional;

public class SampleFormatter {

    public String toString(Sample s) {
        return Optional.ofNullable(s.getValue())
                .map(String::toUpperCase).orElse(null);
    }
}
