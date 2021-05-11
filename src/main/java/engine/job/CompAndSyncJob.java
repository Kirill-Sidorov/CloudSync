package engine.job;

import engine.comp.CompData;

import java.util.TimerTask;

public class CompAndSyncJob extends TimerTask {

    private final CompData leftData;
    private final CompData rightData;

    public CompAndSyncJob(final CompData leftData, final CompData rightData) {
        this.leftData = leftData;
        this.rightData = rightData;
    }

    @Override
    public void run() {

    }
}
