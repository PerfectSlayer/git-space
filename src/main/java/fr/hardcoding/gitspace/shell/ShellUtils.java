package fr.hardcoding.gitspace.shell;

import static java.util.Collections.emptyList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ShellUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static CommandResult run(Path workingDir, String... commands) throws CommandException {
        LOGGER.info("Running command: \"{}\".", Arrays.toString(commands));
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            if (workingDir != null) {
                processBuilder.directory(workingDir.toFile());
            }
            Process process = processBuilder.start();
            List<String> output, error;
            try (BufferedReader reader = process.inputReader()) {
                output = readAllLines(reader);
            }
            try (BufferedReader reader = process.errorReader()) {
                error = readAllLines(reader);
            }
            int returnCode = process.waitFor();
            return new CommandResult(returnCode, output, error);
        } catch (IOException e) {
            throw new CommandException("Failed to run command " + Arrays.toString(commands), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new CommandResult(1, emptyList(), emptyList());
        }
    }

    private static List<String> readAllLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public static CommandResult run(String... command) throws CommandException {
        return run(null, command);
    }

}
