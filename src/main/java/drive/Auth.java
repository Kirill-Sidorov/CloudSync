package drive;

import model.disk.Disk;

/**
 * ����������� ������� ������ ��������� ��������� ������
 */
public interface Auth {
    /**
     * ���������� �������� ��������� ������
     * @return ������������ ���������
     */
    Disk authorize();
}
