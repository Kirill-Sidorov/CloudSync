package model.cloud;

import drive.Auth;
import drive.dropbox.DropboxAuth;
import drive.googledrive.GoogleAuth;

public enum CloudType {
    GOOGLE {
        @Override
        public String tokensDir() { return "tokens/google"; }

        @Override
        public Auth auth(String tokenPath) { return new GoogleAuth(tokensDir() + "/" + tokenPath); }
    },
    DROPBOX {
        @Override
        public String tokensDir() { return "tokens/dropbox"; }

        @Override
        public Auth auth(String tokenPath) { return new DropboxAuth(""); }
    };
    public abstract String tokensDir();
    public abstract Auth auth(String tokenPath);
}
