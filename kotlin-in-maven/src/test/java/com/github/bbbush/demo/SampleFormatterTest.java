package com.github.bbbush.demo;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SampleFormatterTest {

    @Test
    public void shouldFormatS() {
        Sample s = new Sample();
        s.setValue("abc");
        assertThat(new SampleFormatter().toString(s)).isEqualTo("ABC");
    }
}
