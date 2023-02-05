package fr.hardcoding.gitspace.model;

import java.nio.file.Path;

public class Worktree {
    public String name;
    public Path path;
    public String localBranch;
    public String remoteBranch;
    public PullRequest pullRequest;
}
