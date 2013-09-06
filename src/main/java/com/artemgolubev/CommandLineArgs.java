package com.artemgolubev;

import com.lexicalscope.jewel.cli.Option;

public interface CommandLineArgs {
	@Option(description="your username", pattern="[A-Za-z0-9\\_\\.-]+", shortName="U")
	public String getUsername();

	@Option(description="your password", shortName="P")
	public String getPassword();

	@Option(description="author of the searched pull request", defaultToNull=true, shortName="A")
	public String getAuthor();

	@Option(description="commit to be found", pattern="[a-z0-9]{5,40}", shortName="C")
	public String getCommit();

	@Option(description="repository id", pattern="[A-Za-z0-9\\_\\.-]+\\/[A-Za-z0-9\\_\\.-]+", shortName="R")
	public String getRepositoryId();
}