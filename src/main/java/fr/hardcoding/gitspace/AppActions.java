package fr.hardcoding.gitspace;

import java.nio.file.Path;

public interface AppActions {
    void createWorktree();

    boolean createWorktree(String branchName, Path location);
}
