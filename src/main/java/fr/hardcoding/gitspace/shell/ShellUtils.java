package fr.hardcoding.gitspace.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class ShellUtils {
    private static final Logger LOGGER = Logger.getLogger(ShellUtils.class.getName());

    public static CommandResult run(Path workingDir, String... commands) throws CommandException {
        LOGGER.info("Running command: \"" + Arrays.toString(commands) + "\".");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            if (workingDir != null) {
                processBuilder.directory(workingDir.toFile());
            }
            Process process = processBuilder.start();
            List<String> output = new ArrayList<>();
            try (BufferedReader reader = process.inputReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
            int returnCode = process.waitFor(); // TODO Handle return code

            return new CommandResult(returnCode, output);
        } catch (IOException e) {
            throw new CommandException("Failed to run command " + Arrays.toString(commands), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new CommandResult(1, Collections.emptyList());
        }
    }

    public static CommandResult run(String... command) throws CommandException {
        return run(null, command);
    }

}
