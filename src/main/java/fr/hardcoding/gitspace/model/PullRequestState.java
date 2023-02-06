package fr.hardcoding.gitspace.model;

public enum PullRequestState {
    OPEN,
    CLOSED,
    MERGED,
    DRAFT;

    public static PullRequestState parse(String s) {
        for (PullRequestState state : values()) {
            if (state.name().equals(s)) {
                return state;
            }
        }
        throw new IllegalArgumentException("No matching state for " + s);
    }
}
