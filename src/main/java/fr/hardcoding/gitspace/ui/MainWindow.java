package fr.hardcoding.gitspace.ui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import fr.hardcoding.gitspace.AppActions;
import fr.hardcoding.gitspace.model.Worktree;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainWindow extends BasicWindow {

    private static final String TITLE = "Git Space";

    public MainWindow(AppActions actions, WorktreeModel model) {
        super(TITLE);
        setHints(List.of(Window.Hint.FULL_SCREEN));

        WorktreeTable table = new WorktreeTable(model);

        Panel contentPanel = new Panel(new BorderLayout());
        contentPanel.addComponent(table, BorderLayout.Location.CENTER);
        contentPanel.addComponent(buildTooltip(), BorderLayout.Location.BOTTOM);
        setComponent(contentPanel);

        addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
//                    System.out.println(keyStroke);
            }

            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (Objects.equals(keyStroke.getCharacter(), 'c')) {
                    actions.createWorktree();
                } else if (Objects.equals(keyStroke.getCharacter(), 'g')) {
                    Worktree selectedWorktree = model.get(table.getSelectedRow());
                    if (selectedWorktree != null && selectedWorktree.pullRequest != null) {
                        try {
                            Desktop.getDesktop().browse(new URI(selectedWorktree.pullRequest.url()));
                        } catch (IOException | URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (Objects.equals(keyStroke.getCharacter(), 'q')) {
                    close();
                }
            }
        });
    }

    private Component buildTooltip() {
        Panel panel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        panel.addComponent(new Label("(C)reate"));
        panel.addComponent(new Separator(Direction.VERTICAL));
        panel.addComponent(new Label("(R)ebase"));
        panel.addComponent(new Label("(P)ush"));
        panel.addComponent(new Label("(G)itHub"));
        panel.addComponent(new Label("(D)elete"));
        panel.addComponent(new Separator(Direction.VERTICAL));
        panel.addComponent(new Label("(Q)uit"));
        return panel.withBorder(Borders.singleLine("Actions"));
    }
}
