package search;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SearchGitHub.class).in(Singleton.class);
	}
}
