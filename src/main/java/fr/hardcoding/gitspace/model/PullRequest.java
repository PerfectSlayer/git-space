package fr.hardcoding.gitspace.model;

public record PullRequest(int number, String url, PullRequestState state) {
}
