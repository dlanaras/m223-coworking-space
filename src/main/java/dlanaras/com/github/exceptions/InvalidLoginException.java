package dlanaras.com.github.exceptions;

public class InvalidLoginException extends Exception {
    public InvalidLoginException(String explanation) {
        super(explanation);
    }
}
