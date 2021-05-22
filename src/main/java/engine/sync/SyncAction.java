package engine.sync;


import model.entity.Entity;
import model.result.Result;

/**
 * Операция синхронизации
 */
@FunctionalInterface
public interface SyncAction {
    /**
     * Выполнить опреацию
     * @param left Левый файл
     * @param right Правый файл
     * @return Результат операции
     */
    Result execute(final Entity left, final Entity right);
}
