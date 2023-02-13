package fr.hardcoding.gitspace.model;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class Worktree {
    public String name;
    public Path path;
    /**
     * The local branch name, <code>null</code> if detached HEAD
     */
    @Nullable
    public String localBranch;
    public String remoteBranch;
    public PullRequest pullRequest;
}
