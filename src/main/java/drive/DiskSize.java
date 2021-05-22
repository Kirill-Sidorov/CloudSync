package drive;

/**
 * ������ ��������� ������
 */
public interface DiskSize {
    /**
     * ��������� ���������� � ������� ���������
     */
    void request();

    /**
     * �������� ������ ���������
     * @return ������ ���������
     */
    long getTotalSpace();

    /**
     * �������� ��������� ������������
     * @return ��������� ������������
     */
    long getUnallocatedSpace();
}
