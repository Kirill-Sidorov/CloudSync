package drive;

import model.result.DirResult;

import javax.swing.*;

public interface Dir {
    DirResult files(final JProgressBar progress);
}
