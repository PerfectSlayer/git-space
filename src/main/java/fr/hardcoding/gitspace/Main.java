package fr.hardcoding.gitspace;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import fr.hardcoding.gitspace.shell.CommandException;
import fr.hardcoding.gitspace.ui.MainWindow;
import fr.hardcoding.gitspace.ui.WorktreeModel;

import java.io.IOException;
import java.nio.file.Path;

public class Main {


    public static void main(String[] args) {
        new Main().run();

    }

    private void run() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try (TerminalScreen screen = defaultTerminalFactory.createScreen()) {
            screen.startScreen();
            WorktreeModel model = new WorktreeModel(Path.of("."));
            MainWindow window = new MainWindow(model);

            // Create gui and start gui
//            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            gui.addWindowAndWait(window);
            screen.stopScreen();
        } catch (IOException | CommandException e) {
            e.printStackTrace();
        }
    }
}
