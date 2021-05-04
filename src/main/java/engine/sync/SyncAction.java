package engine.sync;


import model.entity.Entity;
import model.result.Result;

@FunctionalInterface
public interface SyncAction {
    Result execute(final Entity left, final Entity right);
}
