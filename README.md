# CloudSync
App for synchronizing local file system with cloud storages. Interface created using Swing library.

Available cloud storages:
* Google Drive;
* Dropbox.

Available languages:
* russian.

### Features:
* connecting cloud storages accounts (Google Drive or Dropbox);
* browsing the catalogs of data storages;
* moving through the directories of various data storages;
* comparison of directories in a separate thread;
* selecting file versions when synchronizing directories;
* choice of direction of synchronization;
* synchronization of directories in a separate thread;
* creating a task to synchronize directories at a specified time.

### Images

![main screen](https://github.com/Kirill-Sidorov/CloudSync/blob/master/images/main.jpg)
|:--:| 
| *Image 1 - Main window* |
![compare](https://github.com/Kirill-Sidorov/CloudSync/blob/master/images/compare.jpg)
| *Image 2 - Compare dirs window* |
![storage manager](https://github.com/Kirill-Sidorov/CloudSync/blob/master/images/storage_manager.jpg)
| *Image 3 - Storage manager window* |
![sync task](https://github.com/Kirill-Sidorov/CloudSync/blob/master/images/sync_task.jpg)
| *Image 4 - Sync dialog window* |

### Required software:
* JDK 8 (jdk1.8.0_201);
* apache maven 3.6.2.

### Installation
1. Clone the repository.
2. Start a command line in the project directory and run the command: 
```console
mvn clean compile assembly:single
```
3. Go to created "target" directory and run "CloudSync-1.0-SNAPSHOT-jar-with-dependencies.jar".
