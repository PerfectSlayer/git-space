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
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
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

            window.addWindowListener(new WindowListenerAdapter() {
                @Override
                public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
//                    System.out.println(keyStroke);
                }

                @Override
                public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                    if (Objects.equals(keyStroke.getCharacter(), 'q')) {
                        window.close();
                    }
                }
            });

            Panel contentPanel = new Panel(new BorderLayout());


            WorktreeTable table = new WorktreeTable();
            contentPanel.addComponent(table, BorderLayout.Location.CENTER);

            contentPanel.addComponent(buildTooltip(), BorderLayout.Location.BOTTOM);

            window.setComponent(contentPanel);

            // Create gui and start gui
//            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            gui.addWindowAndWait(window);
            screen.stopScreen();
        } catch (IOException e) {
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