package drive;

import model.disk.Disk;

/**
 * Авторизация учетной записи облачного хранилища данных
 */
public interface Auth {
    /**
     * Подключить облачное хранилище данных
     * @return Подключенное хранилище
     */
    Disk authorize();
}
