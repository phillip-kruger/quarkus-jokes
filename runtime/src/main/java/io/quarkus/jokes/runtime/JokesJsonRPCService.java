package io.quarkus.jokes.runtime;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provide Jokes
 */
public class JokesJsonRPCService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();

    public Joke getJoke() {
        Joke joke = getRandomJoke();
        User user = getRandomUser();

        // Add a random user
        joke.setUser(user.getFullName());
        joke.setProfilePic(user.getProfileIcon());

        joke.setTimestamp(getTimestamp());

        return joke;

    }

    public Joke addJoke(String user, String setup, String punchline) {
        Joke joke = new Joke();
        joke.setId(Math.abs(random.nextInt()));
        joke.setUser(user);
        joke.setSetup(setup);
        joke.setPunchline(punchline);
        joke.setTimestamp(getTimestamp());
        joke.setType("General");
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
            throw new RuntimeException(ex);
        }
    }

    private User getRandomUser() {
        try {
            return mapper.readValue(new URL("https://randomuser.me/api"), Results.class).results[0];
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Results {
        public User[] results;
    }
}
