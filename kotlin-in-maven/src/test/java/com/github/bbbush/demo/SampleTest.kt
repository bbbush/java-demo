package com.github.bbbush.demo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SampleTest {

    @Test
    fun shouldDoNothing() {
        val sample = Sample()
        assertThat(sample).isNotNull
        sample.value = "abc"
        assertThat(sample.value).isEqualTo("abc")
    }

    @Test
    fun shouldFormatS() {
        val sample = Sample()
        sample.value = "abc"
        assertThat(SampleFormatter().toString(sample)).isEqualTo("ABC")
    }

}
