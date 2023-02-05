package fr.hardcoding.gitspace.shell;

import fr.hardcoding.gitspace.model.PullRequest;
import fr.hardcoding.gitspace.model.PullRequestState;
import fr.hardcoding.gitspace.model.Worktree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class GitCommands {
    public static Path getRootDir(Path dir) throws CommandException {
        CommandResult result = ShellUtils.run("git", "rev-parse", "--show-toplevel");
        String firstLine = result.firstLine();
        if (firstLine.isEmpty()) {
            throw new CommandException("Failed to get root dir for " + dir.toAbsolutePath());
        }
        return Path.of(firstLine);
    }

    public static List<Worktree> listWorktrees(Path rootDir) throws CommandException {
        List<Worktree> worktrees = new ArrayList<>();

        CommandResult result = ShellUtils.run(rootDir, "git", "worktree", "list", "--porcelain");
        List<String> output = result.output();
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
            worktree.localBranch = branch;
            worktrees.add(worktree);
        }
        return worktrees;
    }

    public static String getRemoteBranch(Path branchDir) throws CommandException {
        CommandResult result = ShellUtils.run(branchDir, "git", "rev-parse", "--abbrev-ref", "--symbolic-full-name", "@{u}");
        String firstLine = result.firstLine();
        if (firstLine.isEmpty()) {
            throw new CommandException("Failed to get remote branch for " + branchDir.toAbsolutePath());
        }
        return firstLine;
    }

    public static PullRequest getPr(Path rootDir, String removeBranch) throws CommandException {
        String shortBranchName = removeBranch;
        int index = shortBranchName.lastIndexOf("/");
        if (index != -1) {
            shortBranchName = shortBranchName.substring(index + 1);
        }
        CommandResult result = ShellUtils.run(rootDir, "gh", "pr", "view", shortBranchName);
        if (!result.isSuccessful()) {
            return null; // No PR for this branch
        }
        int number = 0;
        String url = null;
        PullRequestState state = null;
        for (String line : result.output()) {
            System.out.println("line = " + line);
            if (line.startsWith("number:")) {
                number = Integer.parseInt(line.substring(8));
            } else if (line.startsWith("url:")) {
                url = line.substring(5);
            } else if (line.startsWith("state:")) {
                state = PullRequestState.parse(line.substring(7));
            }
        }
        return new PullRequest(number, url, state);
    }
}
