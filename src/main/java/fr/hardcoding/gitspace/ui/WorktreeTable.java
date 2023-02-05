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
    }

    public void sync() {
        TableModel<String> tableModel = getTableModel();
        for (Worktree worktree : model.worktrees) {
            String pullRequest = worktree.pullRequest == null ? "" : "#" + worktree.pullRequest.number() + " " + worktree.pullRequest.state();
            tableModel.addRow(worktree.name, worktree.localBranch, pullRequest);
        }
    }
}
