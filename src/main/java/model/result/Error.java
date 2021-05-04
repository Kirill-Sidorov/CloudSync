package model.result;

import java.util.ResourceBundle;

public enum Error {
    NO {
        @Override
        public String getMessage(ResourceBundle bundle) { return ""; }
    },
    UNKNOWN {
        @Override
        public String getMessage(ResourceBundle bundle) { return "Unknown"; }
    },
    FILE_NOT_FOUND_ERROR {
        @Override
        public String getMessage(ResourceBundle bundle) { return bundle.getString("message.error.file_or_directory_does_not_exist"); }
    },
    FILE_NOT_RUN_ERROR {
        @Override
        public String getMessage(ResourceBundle bundle) { return bundle.getString("message.error.failed_to_run_file"); }
    },
    FILE_NOT_UPLOAD_ERROR {
        @Override
        public String getMessage(ResourceBundle bundle) { return "FILE_NOT_UPLOAD_ERROR"; }//bundle.getString("message.error.file_not_upload"); }
    },
    FILE_NOT_DOWNLOAD_ERROR {
        @Override
        public String getMessage(ResourceBundle bundle) { return "file not download error"; }
    },
    CLOUD_DRIVE_NOT_CONNECTED {
        @Override
        public String getMessage(ResourceBundle bundle) { return bundle.getString("message.error.cloud_drive_not_connected"); }
    },
    NO_CREDENTIALS {
        @Override
        public String getMessage(ResourceBundle bundle) { return bundle.getString("message.error.no_credentials"); }
    },
    NOT_ALL_FILES_WERE_RETRIEVED {
        @Override
        public String getMessage(ResourceBundle bundle) { return bundle.getString("message.error.not.all_files_were_retrieved"); }
    },
    FAILED_GET_DIRECTORY_FILES {
        @Override
        public String getMessage(ResourceBundle bundle) { return "failed_get_dir_files";}//bundle.getString("message.error.failed_get_directory_files"); }
    },
    UPDATE_THREAD_CRASH {
        @Override
        public String getMessage(ResourceBundle bundle) { return "update thread crash"; }
    },
    CURRENT_DISKS_CANNOT_BE_SYNCED {
        @Override
        public String getMessage(ResourceBundle bundle) { return "CURRENT_DISKS_CANNOT_BE_SYNCED"; }
    };

    public abstract String getMessage(ResourceBundle bundle);
}
