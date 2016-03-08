package be.kdg.kandoe.kandoe.exception;

/**
 * Created by Edward on 8/03/2016.
 */
public class UserException extends RuntimeException{
    public UserException(String detailMessage) {
        super(detailMessage);
    }
}
