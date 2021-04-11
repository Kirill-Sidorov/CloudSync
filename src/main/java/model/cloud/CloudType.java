package model.cloud;

import drive.Auth;
import drive.googledrive.GoogleAuth;

public enum CloudType {
    GOOGLE {
        @Override
        public String tokensDir() { return "tokens/google"; }

        @Override
        public Auth auth(String tokenPath) { return new GoogleAuth(tokensDir() + "/" + tokenPath); }
    };
    public abstract String tokensDir();
    public abstract Auth auth(String tokenPath);
}
