package fr.hardcoding.gitspace;

import java.nio.file.Path;
import java.util.List;

public final class GitCommands {
    public static Path getRootDir(Path dir) throws CommandException {
        List<String> output = ShellUtils.run("git", "rev-parse", "--show-toplevel");
        if (output.isEmpty()) {
            throw new CommandException("Failed to get root dir for "+dir.toAbsolutePath());
        }
        return Path.of(output.get(0));
    }
}
