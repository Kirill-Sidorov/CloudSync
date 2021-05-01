package app.logic;

import javax.swing.*;

public enum SyncMode {
    ALL {
        @Override
        public ImageIcon image() { return new ImageIcon(getClass().getResource("/img/all-sync.png")); }
    },
    LEFT {
        @Override
        public ImageIcon image() { return new ImageIcon(getClass().getResource("/img/left-sync.png")); }
    },
    RIGHT {
        @Override
        public ImageIcon image() { return new ImageIcon(getClass().getResource("/img/right-sync.png")); }
    };

    public abstract ImageIcon image();
}
