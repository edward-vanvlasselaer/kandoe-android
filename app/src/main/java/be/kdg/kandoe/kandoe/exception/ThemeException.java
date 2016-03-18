package be.kdg.kandoe.kandoe.exception;

/**
 * Created by Edward on 17/03/2016.
 */
public class ThemeException extends RuntimeException {
    public ThemeException(String detailMessage) {
        super("RUNTIME EXCEPTION!: " + detailMessage);
    }
}
