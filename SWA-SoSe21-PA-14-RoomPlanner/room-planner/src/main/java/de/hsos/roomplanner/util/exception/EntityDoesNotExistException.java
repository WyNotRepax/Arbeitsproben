package de.hsos.roomplanner.util.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Benno Steinkamp
 */
@JsonIgnoreProperties({ "stackTrace", "suppressed" })
public class EntityDoesNotExistException extends Exception {

    private final String message;

    public EntityDoesNotExistException(Class<?> clazz, Object id) {
        this.message = String
                .format("Entity with id %s does not exist for %s", String.valueOf(id), clazz.getSimpleName());
    }

    public EntityDoesNotExistException(Class<?> clazz, long id) { this(clazz, Long.valueOf(id)); }

    @Override
    public String getMessage() { return message; }

    @Override
    public String toString() { return "EntityAlreadyExistsException [message=" + message + "]"; }

}
