import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SampleTest {

    @Test
    fun shouldDoNothing() {
        val sample = Sample()
        assertThat(sample).isNotNull
        sample.s = "abc"
        assertThat(sample.s).isEqualTo("abc")
    }

}
