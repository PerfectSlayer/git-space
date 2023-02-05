package fr.hardcoding.gitspace;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class GitCommands {
    public static Path getRootDir(Path dir) throws CommandException {
        List<String> output = ShellUtils.run("git", "rev-parse", "--show-toplevel");
        if (output.isEmpty()) {
            throw new CommandException("Failed to get root dir for " + dir.toAbsolutePath());
        }
        return Path.of(output.get(0));
    }

    public static List<Worktree> listWorktrees(Path rootDir) throws CommandException {
        List<Worktree> result = new ArrayList<>();

        List<String> output = ShellUtils.run(rootDir, "git", "worktree", "list", "--porcelain");

        int nbLines = output.size();
        if (nbLines % 4 != 0) {
            throw new CommandException("Unexpected output while listing worktrees for " + rootDir.toAbsolutePath());
        }

        for (int i = 0; i < nbLines / 4; i++) {
            String firstLine = output.get(i * 4);
            if (!firstLine.startsWith("worktree ")) {
                throw new CommandException("Failed to read worktree path from " + firstLine);
            }
            Path path = Path.of(firstLine.substring(9));
            String name = path.getFileName().toString();
            String thirdLine = output.get(i * 4 + 2);
            if (!thirdLine.startsWith("branch")) {
                throw new CommandException("Failed to read worktree branch from " + thirdLine);
            }
            String branch = thirdLine.substring(7);

            Worktree worktree = new Worktree();
            worktree.name = name;
            worktree.path = path;
            worktree.branchName = branch;
            result.add(worktree);
        }
        return result;
    }
}
