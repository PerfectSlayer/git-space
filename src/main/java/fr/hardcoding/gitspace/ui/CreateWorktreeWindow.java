package fr.hardcoding.gitspace.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import fr.hardcoding.gitspace.AppActions;

import java.nio.file.Path;
import java.util.List;

import static com.googlecode.lanterna.gui2.GridLayout.Alignment.BEGINNING;
import static com.googlecode.lanterna.gui2.GridLayout.Alignment.FILL;

public class CreateWorktreeWindow extends BasicWindow {
    private static final String BRANCH_NAME_PREFIX = "bbujon/";  // TODO Find how to read / parametrize
    private static final String WORKTREE_LOCATION_PREFIX = "../";
    private final AppActions appActions;
    private final TextBox branchNameTextBox;
    private final TextBox locationTextBox;

    public CreateWorktreeWindow(AppActions appActions) {
        super("Create Worktree");
        this.appActions = appActions;
        setHints(List.of(Hint.CENTERED));

        Panel contentPanel = new Panel();
        contentPanel.setLayoutManager(new GridLayout(3));

        contentPanel.addComponent(new Label("Branch name"));
        this.branchNameTextBox = new TextBox(new TerminalSize(30, 1));
//                .setValidationPattern(Pattern.compile("[0-9]*"))  // TODO Check if the branch name does not already exist
        contentPanel.addComponent(this.branchNameTextBox, GridLayout.createLayoutData(FILL, BEGINNING, false, false, 2, 1));
        this.branchNameTextBox.setTextChangeListener(this::syncFields);

        contentPanel.addComponent(new Label("Location"));
        this.locationTextBox = new TextBox(new TerminalSize(30, 1));
//                .setValidationPattern(Pattern.compile("[0-9]*"))  // TODO Check if the path is valid
        contentPanel.addComponent(this.locationTextBox, GridLayout.createLayoutData(FILL, BEGINNING, false, false, 2, 1));

        contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 0)));
        new Button("Create", this::create).addTo(contentPanel);
        new Button("Abort", this::close).addTo(contentPanel);

        setComponent(contentPanel);
        initializeBranchName();
    }

    private void initializeBranchName() {
        String prefix = BRANCH_NAME_PREFIX;
        String location = WORKTREE_LOCATION_PREFIX + prefix;
        this.branchNameTextBox.setText(prefix);
        this.branchNameTextBox.setCaretPosition(prefix.length());
        this.locationTextBox.setText(location);
        this.locationTextBox.setCaretPosition(location.length());
    }

    private void syncFields(String newText, boolean changedByUserInteraction) {
        String location = this.locationTextBox.getText();
        if (changedByUserInteraction && location.startsWith(WORKTREE_LOCATION_PREFIX)) {
            location = WORKTREE_LOCATION_PREFIX + newText;
            this.locationTextBox.setText(location);
            this.locationTextBox.setCaretPosition(location.length());
        }
    }

    private void create() {
        String branchName = this.branchNameTextBox.getText();
        Path location = Path.of(this.locationTextBox.getText());
        if (this.appActions.createWorktree(branchName, location)) {
            close();
        }
    }
}
