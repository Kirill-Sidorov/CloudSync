package model.cloud;

import drive.Auth;
import drive.dropbox.DropboxAuth;
import drive.google.GoogleAuth;
import drive.remoteserver.RemoteServerAuth;
import model.disk.Disk;

import javax.swing.*;

public enum CloudType {
    GOOGLE {
        @Override
        public String tokensDir() { return "tokens/google"; }

        @Override
        public Auth auth(String tokenPath) { return new GoogleAuth(tokensDir() + "/" + tokenPath); }

        @Override
        public ImageIcon image() { return new ImageIcon(getClass().getResource("/img/google-icon.png")); }
    },
    DROPBOX {
        @Override
        public String tokensDir() { return "tokens/dropbox"; }

        @Override
        public Auth auth(String tokenPath) { return new DropboxAuth(""); }

        @Override
        public ImageIcon image() { return new ImageIcon(getClass().getResource("/img/dropbox-icon.png")); }
    },
    REMOTE_SERVER {
        @Override
        public String tokensDir() {
            return "";
        }

        @Override
        public Auth auth(String tokenPath) {
            return new RemoteServerAuth();
        }

        @Override
        public ImageIcon image() {
            return new ImageIcon(getClass().getResource("/img/remote-server-icon.png"));
        }
    };
    public abstract String tokensDir();
    public abstract Auth auth(String tokenPath);
    public abstract ImageIcon image();

}
