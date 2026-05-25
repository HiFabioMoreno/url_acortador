package fabio.dev.url_shortener.excepciones;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
