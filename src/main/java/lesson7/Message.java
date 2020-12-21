package lesson7;

import java.util.Optional;

public class Message {
    private final String message;
    private String user;
    private String description;

    public Message(String message, String user, String description) {
        this.message = message;
        this.user = user;
        this.description = description;
    }

    public Message(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getUser() {
        return Optional.ofNullable(user).orElse("<Server>");
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
