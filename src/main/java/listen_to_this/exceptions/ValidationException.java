package listen_to_this.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {

        super("Error occurred with the payload.");
        this.errorsMessages = errorsMessages;
    }
}