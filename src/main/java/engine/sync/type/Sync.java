package engine.sync.type;

import app.task.LabelUpdating;
import model.result.SyncResult;

import java.util.ResourceBundle;

/**
 * ������������� ���������
 */
public interface Sync {
    /**
     * ����������������
     * @param labelUpdating ���������� ���������� � ���������� �������������
     * @param bundle ������ ���������
     * @return ��������� �������������
     */
    SyncResult sync(final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
