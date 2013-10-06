package com.artemgolubev;

import java.io.IOException;

public interface PullRequestFinder {

	public abstract String findPullRequestHtmlUrl(final Parameters parameters) throws IOException;

}