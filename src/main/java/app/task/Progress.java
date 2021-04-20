package app.task;

@FunctionalInterface
public interface Progress {
    void increase(int value);
}
