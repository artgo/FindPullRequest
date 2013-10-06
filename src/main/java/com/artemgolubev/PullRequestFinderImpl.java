package com.artemgolubev;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullRequestFinderImpl implements PullRequestFinder {
	private static final Logger log = LoggerFactory.getLogger(PullRequestFinderImpl.class);
	private static final String CLOSED = "closed";

	/* (non-Javadoc)
	 * @see com.artemgolubev.PullRequestFinder#findPullRequestHtmlUrl(com.artemgolubev.Parameters)
	 */
	@Override
	public String findPullRequestHtmlUrl(final Parameters parameters) throws IOException {
		final PullRequestService pullRequestService = new PullRequestService();
		pullRequestService.getClient().setCredentials(parameters.getUsername(), parameters.getPassword());

		final RepositoryId repositoryId = RepositoryId.createFromId(parameters.getRepositoryId());

		log.info("Authenticating and loading pull requests list...");

		final List<PullRequest> closedPullRequests = pullRequestService.getPullRequests(repositoryId, CLOSED);

		final String author = parameters.getAuthor();
		final String commitSHA = parameters.getCommitSHA();
		final boolean authorIsSet = StringUtils.isNotBlank(author);

		log.info("Loaded. Starting...");

		for (final PullRequest pullRequest : closedPullRequests) {
			log.info("On {}", pullRequest.getNumber());
			if (authorIsSet && author.equalsIgnoreCase(pullRequest.getUser().getLogin())) {
				log.info("Processing {}", pullRequest.getNumber());
				final List<RepositoryCommit> commits = pullRequestService.getCommits(repositoryId, pullRequest.getNumber());
				log.info("Checking commits for {}", pullRequest.getNumber());

				for (RepositoryCommit repositoryCommit : commits) {
					if (commitSHA.equalsIgnoreCase(repositoryCommit.getSha())) {
						log.info("Found pull request:\n{}", pullRequest.getHtmlUrl());
						return pullRequest.getHtmlUrl();
					}
				}
			}
		}

		log.info("Pull Request Not Found!");
		return null;
	}
}
