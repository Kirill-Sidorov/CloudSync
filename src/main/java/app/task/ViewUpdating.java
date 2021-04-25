package app.task;

import model.result.Result;

@FunctionalInterface
public interface ViewUpdating {
    void result(Result result);
}
