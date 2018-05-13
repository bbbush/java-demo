import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class Helper {

    private static Logger logger = LoggerFactory.getLogger(Helper.class);

    public static void runTasks(Node node, Function<Node, Collection<Node>> expand) {
        node.children.addAll(expand.apply(node));
        node.children.forEach(n -> runTasks(n, expand));
    }

    public static Stream<Node> flatten(Node node) {
        return node.children.isEmpty() ? Stream.of(node) :
                node.children.stream().flatMap(Helper::flatten);
    }

    public static void runForkJoinTasks(Node node, Function<Node, Collection<Node>> expand, ForkJoinPool fjPool) {
        fjPool.submit(() -> runForkJoinTasks(node, expand)).join();
    }

    private static void runForkJoinTasks(Node node, Function<Node, Collection<Node>> expand) {
        node.children.addAll(expand.apply(node));
        List<? extends ForkJoinTask<?>> tasks = node.children.stream().map(n -> RecursiveAction.adapt(() -> {
            runForkJoinTasks(n, expand);
        }).fork()).collect(toList());
        tasks.forEach(ForkJoinTask::join);
    }

    public static void runThreadPoolTasks(Node node, Function<Node, Collection<Node>> expand, ExecutorService executorService) {
        CompletableFuture.runAsync(() -> runThreadPoolTasksRecursive(node, expand, executorService), executorService).join();
    }

    private static void runThreadPoolTasksRecursive(Node node, Function<Node, Collection<Node>> expand, ExecutorService executorService) {
        node.children.addAll(expand.apply(node));
        List<CompletableFuture<Void>> tasks = node.children.stream().map(n -> CompletableFuture.runAsync(() -> {
            runThreadPoolTasksRecursive(n, expand, executorService);
        }, executorService)).collect(toList());
        tasks.forEach(CompletableFuture::join);
    }

    public static Function<Node, Collection<Node>> makeExpandFunction(int maxLevel) {
        AtomicInteger counter = new AtomicInteger();
        return node -> {
            if (node.level > maxLevel) {
                return emptyList();
            }
            try {
                logger.info("expanding {}", counter.incrementAndGet());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("sleep", e);
                }
                Node h1 = new Node();
                Node h2 = new Node();
                List<Node> list = Arrays.asList(h1, h2);
                list.forEach(n -> n.level = node.level + 1);
                return list;
            } finally {
                counter.decrementAndGet();
            }
        };
    }

    public static class Node {
        private final List<Node> children = new ArrayList<>();
        private int level;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}

