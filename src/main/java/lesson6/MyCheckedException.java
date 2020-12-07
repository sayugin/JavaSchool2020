package lesson6;

public class MyCheckedException extends Exception{
    private int code;
    private String description;

    public MyCheckedException(int code, String description, String message) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
