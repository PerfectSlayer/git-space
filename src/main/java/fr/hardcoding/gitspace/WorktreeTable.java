package fr.hardcoding.gitspace;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;

public class WorktreeTable extends Table<String> {

    public WorktreeTable() {
        super("Worktree", "Branch", "PR");
        TableModel<String> tableModel = getTableModel();

        for (int i=0; i<30; i++) {
            tableModel.addRow("Worktree-"+i, "/bbujon/change-"+i, "#10"+i);
        }
    }
}
