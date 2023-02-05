package fr.hardcoding.gitspace;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;

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
            tableModel.addRow(worktree.name, worktree.branchName, "#10");
        }
    }
}
