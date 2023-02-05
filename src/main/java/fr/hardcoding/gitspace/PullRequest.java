package fr.hardcoding.gitspace;

public record PullRequest(int number, String url, PullRequestState state) {
}
