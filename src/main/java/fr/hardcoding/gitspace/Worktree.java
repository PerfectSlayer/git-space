package fr.hardcoding.gitspace;

import java.nio.file.Path;

public class Worktree {
    String name;
    Path path;
    String localBranch;
    String remoteBranch;
    PullRequest pullRequest;
}
