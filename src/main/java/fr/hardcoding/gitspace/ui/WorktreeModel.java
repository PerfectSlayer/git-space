package fr.hardcoding.gitspace.ui;

import fr.hardcoding.gitspace.model.Worktree;
import fr.hardcoding.gitspace.shell.CommandException;
import fr.hardcoding.gitspace.shell.GitCommands;

import java.nio.file.Path;
import java.util.List;

public class WorktreeModel {
    final Path rootDir;
    final List<Worktree> worktrees;

    public WorktreeModel(Path rootDir) throws CommandException {
        this.rootDir = rootDir;
        this.worktrees = GitCommands.listWorktrees(this.rootDir);
        for (Worktree worktree : this.worktrees) {
            worktree.remoteBranch = GitCommands.getRemoteBranch(worktree.path);
            if (worktree.remoteBranch != null) {
                worktree.pullRequest = GitCommands.getPr(this.rootDir, worktree.remoteBranch);
            }
        }
    }

    public void create(String branchName, Path location) throws CommandException {
        if (!location.isAbsolute()) {
            location = this.rootDir.resolve(location).normalize();
        }
        GitCommands.createWorktree(branchName, location);
    }

    public Worktree get(int index) {
        if (index < 0 || index >= this.worktrees.size()) {
            return null;
        }
        return this.worktrees.get(index);
    }
}
