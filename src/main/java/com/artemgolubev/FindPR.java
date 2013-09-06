package com.artemgolubev;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

public class FindPR {
	private static final String CLOSED = "closed";

	public static void main(final String[] args) throws IOException {
		try {
			final CommandLineArgs clArgs = CliFactory.parseArguments(CommandLineArgs.class, args);
			final FindPR findPr = new FindPR();
			findPr.process(clArgs);
		} catch (ArgumentValidationException e) {
			System.out.println(e.getMessage());
		}
	}

	private void process(final CommandLineArgs clArgs) throws IOException {
		final PullRequestService pullRequestService = new PullRequestService();
		pullRequestService.getClient().setCredentials(clArgs.getUsername(), clArgs.getPassword());

		final RepositoryId repositoryId = RepositoryId.createFromId(clArgs.getRepositoryId());

		System.out.println("Authenticating and loading pull requests list...");

		final List<PullRequest> closedPullRequests = pullRequestService.getPullRequests(repositoryId, CLOSED);

		final String author = clArgs.getAuthor();
		final String commitSHA = clArgs.getCommit();
		final boolean authorIsSet = StringUtils.isNotBlank(author);

		System.out.println("Loaded. Starting...");

		for (final PullRequest pullRequest : closedPullRequests) {
			System.out.println("On " + pullRequest.getNumber());
			if (authorIsSet && author.equalsIgnoreCase(pullRequest.getUser().getLogin())) {
				System.out.println("Processing " + pullRequest.getNumber());
				final List<RepositoryCommit> commits = pullRequestService.getCommits(repositoryId,
						pullRequest.getNumber());
				System.out.println("Checking commits for " + pullRequest.getNumber());
				for (RepositoryCommit repositoryCommit : commits) {
					if (commitSHA.equalsIgnoreCase(repositoryCommit.getSha())) {
						System.out.println("Found pull request:\n" + pullRequest.getHtmlUrl());
						return;
					}
				}
			}
		}

		System.out.println("Pull Request Not Found!");
	}
}
