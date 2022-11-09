package io.quarkus.jokes.deployment;

import java.util.List;

import io.quarkus.builder.item.SimpleBuildItem;
import io.quarkus.jokes.runtime.Joke;

/**
 * Just some random jokes
 */
public final class JokesBuildItem extends SimpleBuildItem {
    private final List<Joke> jokes;

    public JokesBuildItem(List<Joke> jokes) {
        this.jokes = jokes;
    }

    public List<Joke> getJokes() {
        return this.jokes;
    }
}
