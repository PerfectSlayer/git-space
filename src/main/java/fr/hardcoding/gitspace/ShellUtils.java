package fr.hardcoding.gitspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ShellUtils {

    public static List<String> run(Path workingDir, String... commands) throws CommandException {
        List<String> output = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            if (workingDir !=null) {
                processBuilder.directory(workingDir.toFile());
            }
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
            int returnCode = process.waitFor(); // TODO Handle return code

        } catch (IOException e) {
            throw new CommandException("Failed to run command "+ Arrays.toString(commands), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return output;
    }

    public static List<String> run(String... command) throws CommandException {
        return run(null, command);
    }
}
