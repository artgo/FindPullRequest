package com.artemgolubev;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

public class FindPR {
	private static final Logger log = LoggerFactory.getLogger(FindPR.class);
	private final PullRequestFinder pullRequestFinder;

	public FindPR(final PullRequestFinder pullRequestFinder) {
		this.pullRequestFinder = pullRequestFinder;
	}

	public static void main(final String[] args) throws IOException {
		try {
			final Parameters clArgs = CliFactory.parseArguments(Parameters.class, args);
			final PullRequestFinder pullRequestFinder = new PullRequestFinderImpl();
			final FindPR findPr = new FindPR(pullRequestFinder);
			findPr.process(clArgs);
		} catch (ArgumentValidationException e) {
			log.error(e.getMessage());
		}
	}

	private void process(final Parameters clArgs) throws IOException {
		final String result = pullRequestFinder.findPullRequestHtmlUrl(clArgs);
		if (StringUtils.isBlank(result)) {
			log.warn("Pull request not found");
		} else {
			log.warn("Found pull request {}", result);
		}
	}
}
