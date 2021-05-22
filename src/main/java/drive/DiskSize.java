package drive;

/**
 * Размер хранилища данных
 */
public interface DiskSize {
    /**
     * Запросить информацию о размере хранилища
     */
    void request();

    /**
     * Получить размер хранилища
     * @return Размер хранилища
     */
    long getTotalSpace();

    /**
     * Получить незанятое пространство
     * @return Незанятое пространство
     */
    long getUnallocatedSpace();
}
