package engine.sync;


import model.entity.Entity;
import model.result.Result;

/**
 * �������� �������������
 */
@FunctionalInterface
public interface SyncAction {
    /**
     * ��������� ��������
     * @param left ����� ����
     * @param right ������ ����
     * @return ��������� ��������
     */
    Result execute(final Entity left, final Entity right);
}
