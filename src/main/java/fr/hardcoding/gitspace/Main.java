package fr.hardcoding.gitspace;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import fr.hardcoding.gitspace.model.Worktree;
import fr.hardcoding.gitspace.shell.CommandException;
import fr.hardcoding.gitspace.ui.WorktreeModel;
import fr.hardcoding.gitspace.ui.WorktreeTable;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    private static final String TITLE = "Git Space";

    public static void main(String[] args) {
        new Main().run();

    }


    private void run() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try (TerminalScreen screen = defaultTerminalFactory.createScreen()) {
            screen.startScreen();

            // Create window to hold the panel
            BasicWindow window = new BasicWindow(TITLE);

            window.setHints(List.of(Window.Hint.FULL_SCREEN));



            Panel contentPanel = new Panel(new BorderLayout());

            WorktreeModel model = new WorktreeModel(Path.of("."));
            WorktreeTable table = new WorktreeTable(model);
            contentPanel.addComponent(table, BorderLayout.Location.CENTER);

            contentPanel.addComponent(buildTooltip(), BorderLayout.Location.BOTTOM);

            window.setComponent(contentPanel);

            window.addWindowListener(new WindowListenerAdapter() {
                @Override
                public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
//                    System.out.println(keyStroke);
                }

                @Override
                public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                    if (Objects.equals(keyStroke.getCharacter(), 'g')) {
                        Worktree selectedWorktree = model.get(table.getSelectedRow());
                        if (selectedWorktree != null && selectedWorktree.pullRequest != null) {
                            try {
                                Desktop.getDesktop().browse(new URI(selectedWorktree.pullRequest.url()));
                            } catch (IOException | URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (Objects.equals(keyStroke.getCharacter(), 'q')) {
                        window.close();
                    }
                }
            });

            // Create gui and start gui
//            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            gui.addWindowAndWait(window);
            screen.stopScreen();
        } catch (IOException | CommandException e) {
            e.printStackTrace();
        }
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