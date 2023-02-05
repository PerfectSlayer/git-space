package fr.hardcoding.gitspace;

import java.nio.file.Path;

public class WorktreeModel {
    final Path rootDir;

    public WorktreeModel(Path rootDir) {
        this.rootDir = rootDir;
    }


    public static WorktreeModel fromDir(Path dir) throws CommandException {
        return new WorktreeModel(GitCommands.getRootDir(dir));
    }
}
