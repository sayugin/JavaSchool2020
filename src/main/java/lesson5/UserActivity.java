package lesson5;

public class UserActivity extends LogEntry{

    private final String login;

    public UserActivity(String message, String login){
        super(message);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return String.format("%d. %s: %s - %s", this.getId(), this.getCreated(), this.getLogin(), this.getMessage());
    }
}
