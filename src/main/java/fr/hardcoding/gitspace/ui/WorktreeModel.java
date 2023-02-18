package fr.hardcoding.gitspace.ui;

import fr.hardcoding.gitspace.model.Worktree;
import fr.hardcoding.gitspace.shell.CommandException;
import fr.hardcoding.gitspace.shell.GitCommands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WorktreeModel {
    final Path rootDir;
    final List<Worktree> worktrees;
    final List<WorktreeModelListener> listeners;

    public WorktreeModel(Path rootDir) {
        this.rootDir = rootDir;
        this.worktrees = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public void load() {
        try {
            List<Worktree> worktrees = GitCommands.listWorktrees(this.rootDir);
            for (Worktree worktree : worktrees) {
                worktree.remoteBranch = GitCommands.getRemoteBranch(worktree.path);
                if (worktree.remoteBranch != null && !"master".equals(worktree.name)) {
                    worktree.pullRequest = GitCommands.getPr(this.rootDir, worktree.remoteBranch);
                }
                this.worktrees.add(worktree);
                for (WorktreeModelListener listener : this.listeners) {
                    listener.worktreeAdded(worktree);
                }
            }
        } catch (CommandException e) {
            e.printStackTrace();
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

    public void addListener(WorktreeModelListener listener) {
        this.listeners.add(listener);
    }
}
