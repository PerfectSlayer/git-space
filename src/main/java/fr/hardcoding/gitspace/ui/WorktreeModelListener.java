package fr.hardcoding.gitspace.ui;

import fr.hardcoding.gitspace.model.Worktree;

public interface WorktreeModelListener {
    void worktreeAdded(Worktree worktree);
    void worktreeRemoved(Worktree worktree);
}
