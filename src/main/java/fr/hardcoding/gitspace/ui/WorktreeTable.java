package fr.hardcoding.gitspace.ui;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import fr.hardcoding.gitspace.model.Worktree;

public class WorktreeTable extends Table<String> {
    private final WorktreeModel model;

    public WorktreeTable(WorktreeModel model) {
        super("Worktree", "Branch", "PR");
        this.model = model;
        sync();
        model.addListener(new WorktreeModelListener() {
            @Override
            public void worktreeAdded(Worktree worktree) {
                addWorktree(worktree);
            }

            @Override
            public void worktreeRemoved(Worktree worktree) {
                sync(); // TODO
            }
        });
    }

    public void sync() {
        TableModel<String> tableModel = getTableModel();
        tableModel.clear();
        this.model.worktrees.forEach(this::addWorktree);
    }

    private void addWorktree(Worktree worktree) {
        TableModel<String> tableModel = getTableModel();
        String pullRequest = worktree.pullRequest == null ? "" : "#" + worktree.pullRequest.number() + " " + worktree.pullRequest.state();
        tableModel.addRow(worktree.name, worktree.localBranch, pullRequest);
    }
}
