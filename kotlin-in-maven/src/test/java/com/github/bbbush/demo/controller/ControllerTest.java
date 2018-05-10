package com.github.bbbush.demo.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerTest {

    @Test
    public void shouldFormatS() {
        assertThat(new Controller().format("abc")).isEqualTo("ABC");
    }
}
