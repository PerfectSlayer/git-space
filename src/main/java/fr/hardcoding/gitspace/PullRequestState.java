package fr.hardcoding.gitspace;

public enum PullRequestState {
    OPEN,
    CLOSED,
    MERGED;

    public static PullRequestState parse(String s) {
        for (PullRequestState state : values()) {
            if (state.name().equals(s)) {
                return state;
            }
        }
        throw new IllegalArgumentException("No matching state for " + s);
    }
}
