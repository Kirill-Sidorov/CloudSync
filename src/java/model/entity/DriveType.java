package model.entity;

import drive.adapter.AdapterManageable;

public enum DriveType {
    LOCAL {
        @Override
        public AdapterManageable getDataManager() {
            return null;
        }
    };
    protected AdapterManageable driveDataManager;

    public abstract AdapterManageable getDataManager();
    //public abstract DownloadTask getDownloadTask(List<FileEntity> files);
}
