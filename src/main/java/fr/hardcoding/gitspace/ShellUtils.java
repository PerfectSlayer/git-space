package fr.hardcoding.gitspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ShellUtils {

    public static List<String> run(String... command) {
        List<String> output = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = process.inputReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
