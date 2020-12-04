package lesson5;

public class UADetails extends UserActivity{

    private String description;

    public UADetails(String message, String login, String description) {
        super(message, login);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%d. %s: %s - %s %s", this.getId(), this.getCreated(), this.getLogin(), this.getMessage(), this.getDescription());
    }
}
