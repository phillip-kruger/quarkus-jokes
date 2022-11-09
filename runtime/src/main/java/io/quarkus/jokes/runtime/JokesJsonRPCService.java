package io.quarkus.jokes.runtime;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provide Jokes
 */
public class JokesJsonRPCService {
    private final ObjectMapper mapper = new ObjectMapper();

    public Joke getJoke() {
        Joke joke = getRandomJoke();
        User user = getRandomUser();

        // Add a random user
        joke.setUser(user.getFullName());
        joke.setProfilePic(user.getProfileIcon());

        // Add a timestamp
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
        timestamp = timestamp.substring(0, timestamp.lastIndexOf("."));
        joke.setTimestamp(timestamp);

        System.out.println(joke);

        return joke;

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
