package de.hsos.roomplanner.util.exception;

/**
 * @author Benno Steinkamp
 */
public class UserNotFoundException extends Exception {

    private final String message;

    public UserNotFoundException(String username) {
        this.message = String.format("User with name %s could not be found", username);
    }

    @Override
    public String toString() { return "UserNotFoundException [message=" + message + "]"; }

    @Override
    public String getMessage() { return message; }

}
