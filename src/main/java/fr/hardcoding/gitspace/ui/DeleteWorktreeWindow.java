package fr.hardcoding.gitspace.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import fr.hardcoding.gitspace.AppActions;
import fr.hardcoding.gitspace.model.Worktree;

import java.util.HashSet;
import java.util.List;

import static com.googlecode.lanterna.gui2.GridLayout.Alignment.BEGINNING;
import static com.googlecode.lanterna.gui2.GridLayout.Alignment.FILL;
import static fr.hardcoding.gitspace.AppActions.DeletionOptions.GIT_BRANCH;
import static fr.hardcoding.gitspace.AppActions.DeletionOptions.LOCAL_FILES;

public class DeleteWorktreeWindow extends BasicWindow {
    private final AppActions appActions;
    private final Worktree worktree;
    private final CheckBoxList<AppActions.DeletionOptions> deleteOptionList;

    public DeleteWorktreeWindow(AppActions appActions, Worktree worktree) {
        super("Delete Worktree");
        this.appActions = appActions;
        this.worktree = worktree;
        setHints(List.of(Hint.CENTERED));

        Panel contentPanel = new Panel();
        contentPanel.setLayoutManager(new GridLayout(2));

        this.deleteOptionList = new CheckBoxList<>(new TerminalSize(30, 2));
        this.deleteOptionList.addItem(LOCAL_FILES, true);
        this.deleteOptionList.addItem(GIT_BRANCH, true);
        contentPanel.addComponent(
                this.deleteOptionList,
                GridLayout.createLayoutData(FILL, BEGINNING, false, false, 2, 1)
        );

        new Button("Delete", this::delete).addTo(contentPanel);
        new Button("Abort", this::close).addTo(contentPanel);

        setComponent(contentPanel);
    }

    private void delete() {
        var options = new HashSet<>(this.deleteOptionList.getCheckedItems());
        if (this.appActions.deleteWorktree(this.worktree, options)) {
            close();
        }
    }
}
