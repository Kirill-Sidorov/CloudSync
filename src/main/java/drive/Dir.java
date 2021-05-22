package drive;

import app.task.Progress;
import app.task.TaskState;
import model.result.DirResult;
import model.result.EntityResult;

/**
 * �������
 */
public interface Dir {
    /**
     * �������� ����� ��������
     * @param progress �������� ���������� ���������
     * @param state ��������� ������ (�������� ��� ���)
     * @return ��������� ��������� ������
     */
    DirResult getFiles(final Progress progress, final TaskState state);

    /**
     * �������� ������� ������
     * @param dirName ��� ��������
     * @return ��������� ��������� ��������
     */
    EntityResult getDirInto(final String dirName);

    /**
     * ������ ���� ������
     * @param fileName ��� �����
     * @return ��������� ������ �����
     */
    EntityResult searchFileInto(final String fileName);
}
