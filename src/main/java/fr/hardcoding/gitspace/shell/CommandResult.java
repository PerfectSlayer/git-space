package fr.hardcoding.gitspace.shell;

import java.util.List;

public record CommandResult(int returnCode, List<String> output, List<String> error) {
    public boolean isSuccessful() {
        return returnCode == 0;
    }

    public String firstLine() {
        if (this.output.isEmpty()) {
            return "";
        }
        return this.output.get(0);
    }
}
