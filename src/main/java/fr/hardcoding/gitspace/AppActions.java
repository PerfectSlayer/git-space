package fr.hardcoding.gitspace;

import fr.hardcoding.gitspace.model.Worktree;

import java.nio.file.Path;
import java.util.Set;

public interface AppActions {
    void promptWorktreeCreation();

    boolean createWorktree(String branchName, Path location);

    void promptWorktreeDeletion(Worktree worktree);

    boolean deleteWorktree(Worktree worktree, Set<DeletionOptions> options);

    void quit();

    enum DeletionOptions {
        LOCAL_FILES("Delete local files"),  // TODO Not really optional
        GIT_BRANCH("Delete git branch");

        private final String label;

        DeletionOptions(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
}
