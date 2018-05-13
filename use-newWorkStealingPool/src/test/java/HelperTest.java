import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newWorkStealingPool;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

class HelperTest {

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldExpandNode() {
        Helper.Node node = new Helper.Node();
        Helper.runTasks(node, Helper.makeExpandFunction(3));
        List<Helper.Node> list = Helper.flatten(node).collect(toList());
        // 15s, 1+2+4+8
        assertThat(list.size()).isEqualTo(16);
    }

    @Test
    public void shouldExpandNodeWithForkJoinPool() {
        Helper.Node node = new Helper.Node();
        Helper.runForkJoinTasks(node, Helper.makeExpandFunction(3), new ForkJoinPool(8));
        List<Helper.Node> list = Helper.flatten(node).collect(toList());
        // ideally 4s, 1+1+1+1, but only seen 5s (less than 8 expanding at once)
        assertThat(list.size()).isEqualTo(16);
    }

    @Test
    public void shouldExpandNodeWithWorkStealingExecutorService() {
        Helper.Node node = new Helper.Node();
        Helper.runThreadPoolTasks(node, Helper.makeExpandFunction(3), newWorkStealingPool(8)); // same behavior as new ForkJoinPool(8)
        List<Helper.Node> list = Helper.flatten(node).collect(toList());
        // ideally 4s, 1+1+1+1, but only seen 10s (only 1 or 2 expanding at once)
        assertThat(list.size()).isEqualTo(16);
    }

    @Test
    public void shouldExpandNodeWithNonWorkStealingExecutorService() {
        Helper.Node node = new Helper.Node();
        Helper.runThreadPoolTasks(node, Helper.makeExpandFunction(3), newFixedThreadPool(16));
        List<Helper.Node> list = Helper.flatten(node).collect(toList());
        // will block
        assertThat(list.size()).isEqualTo(16);
    }

}
