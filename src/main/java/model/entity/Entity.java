package model.entity;

import java.time.LocalDateTime;

public interface Entity {
    String path();
    String name();
    LocalDateTime modifiedDate();
    Long size();
    String typeName();
    boolean isDirectory();
}
