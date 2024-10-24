package io.quarkus.jokes.runtime;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.core.http.Cookie;

/**
 * Provide Jokes for JSON-RPC Endpoint
 */
public class JokesJsonRPCService {

    private final BroadcastProcessor<Joke> jokeStream = BroadcastProcessor.create();
    private final BroadcastProcessor<Integer> countStream = BroadcastProcessor.create();
    private final BroadcastProcessor<Joke> jokeLog = BroadcastProcessor.create();
    private final BroadcastProcessor<Joke> errorStream = BroadcastProcessor.create();
    private static int numberOfJokesTold = 10;

    private Set<Cookie> cookies = Set.of();

    @PostConstruct
    void init() {
        Multi.createFrom().ticks().every(Duration.ofHours(4)).subscribe().with((item) -> {
            jokeStream.onNext(getJoke());
        });

        Multi.createFrom().ticks().every(Duration.ofMinutes(1)).subscribe().with((item) -> {
            errorStream.onError(new Exception("Simulate some exception"));
        });

    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

    public Set<Cookie> getCookies() {
        return this.cookies;
    }

    public Multi<Joke> streamJokes() {
        return jokeStream;
    }

    public Multi<Joke> errorStream() {
        return errorStream;
    }

    public String getDynamicLink() {
        // Here get your link dynamicly
        return "/q/info";
    }

    public Joke getJoke() {
        numberOfJokesTold++;
        countStream.onNext(numberOfJokesTold);
        Joke joke = fetchRandomJoke();
        jokeLog.onNext(joke);
        return joke;
    }

    public Multi<Integer> getNumberOfJokesTold() {
        return countStream;
    }

    public Multi<Joke> jokeLog() {
        return jokeLog;
    }

    public List<Joke> initJokes(Integer size) {
        List<Joke> initialJokes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            initialJokes.add(getJoke());
        }
        return initialJokes;
    }

    private Joke fetchRandomJoke() {
        Joke joke = getRandomJoke();
        User user = getRandomUser();

        // Add a random user
        joke.setUser(user.getFullName());
        joke.setProfilePic(user.getProfileIcon());

        joke.setTimestamp(getTimestamp());

        return joke;
    }

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
        return timestamp.substring(0, timestamp.lastIndexOf("."));
    }

    private Joke getRandomJoke() {
        try {
            return mapper.readValue(new URL("https://official-joke-api.appspot.com/jokes/random/"), Joke.class);
        } catch (IOException ex) {
            // joke service not availalbe. Fallback to hardcoded
            return backupJoke;
        }
    }

    private User getRandomUser() {
        try {
            return mapper.readValue(new URL("https://randomuser.me/api"), Results.class).results[0];
        } catch (IOException ex) {
            // user service not availalbe. Fallback to hardcoded
            return backupUser;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Results {
        public User[] results;
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private static User backupUser = new User();
    private static Joke backupJoke = new Joke();
    static {
        backupUser.name = new User.Name();
        backupUser.name.first = "Phillip";
        backupUser.name.last = "Kruger";
        backupUser.name.title = "Mr.";
        backupUser.picture = new User.Picture();
        backupUser.picture.thumbnail = "https://avatars.githubusercontent.com/u/6836179?v=4";

        backupJoke.setId(0);
        backupJoke.setSetup("I'm afraid for the calendar.");
        backupJoke.setPunchline("Its days are numbered.");
    }
}
