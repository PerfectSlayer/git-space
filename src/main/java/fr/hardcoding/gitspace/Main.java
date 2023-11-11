package fr.hardcoding.gitspace;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import fr.hardcoding.gitspace.model.Worktree;
import fr.hardcoding.gitspace.shell.CommandException;
import fr.hardcoding.gitspace.ui.CreateWorktreeWindow;
import fr.hardcoding.gitspace.ui.DeleteWorktreeWindow;
import fr.hardcoding.gitspace.ui.MainWindow;
import fr.hardcoding.gitspace.ui.WorktreeModel;
import fr.hardcoding.gitspace.ui.WorktreeModel.DeletionChecks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements AppActions {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ExecutorService executor;
    private MultiWindowTextGUI gui;
    private WorktreeModel model;

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        this.executor = Executors.newSingleThreadExecutor();
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try (TerminalScreen screen = defaultTerminalFactory.createScreen()) {
            screen.startScreen();
            Path rootDir = Path.of(".").toAbsolutePath().normalize(); // TODO Fetch Git root dir
            this.model = new WorktreeModel(rootDir);
            this.executor.submit(this.model::load);
            MainWindow window = new MainWindow(this, this.model);
            // Create gui and start gui
//            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            this.gui.addWindowAndWait(window);
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void promptWorktreeCreation() {
        this.gui.addWindow(new CreateWorktreeWindow(this));
    }

    @Override
    public boolean createWorktree(String branchName, Path location) {
        try {
            this.model.create(branchName, location);
            return true;
        } catch (CommandException e) {
            LOGGER.warn("Failed to crete worktree", e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void promptWorktreeDeletion(Worktree worktree) {
        try {
            DeletionChecks checks = this.model.checkForRemoval(worktree);
            this.gui.addWindow(new DeleteWorktreeWindow(this, worktree, checks));
        } catch (CommandException e) {
            LOGGER.warn("Failed to prompt for worktree deletion", e);
        }
    }

    @Override
    public boolean deleteWorktree(Worktree worktree, Set<DeletionOptions> options) {
        try {
            this.model.delete(worktree, options);
            return true;
        } catch (CommandException e) {
            LOGGER.warn("Failed to delete worktree", e);
            return false;
        }
    }

    @Override
    public void quit() {
        this.executor.shutdown();
    }
}
