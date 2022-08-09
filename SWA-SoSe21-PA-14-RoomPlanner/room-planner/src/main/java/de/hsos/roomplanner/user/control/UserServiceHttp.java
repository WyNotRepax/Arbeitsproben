package de.hsos.roomplanner.user.control;

import javax.validation.constraints.NotBlank;

import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;

/**
 * @author Christoph Freimuth
 */

public interface UserServiceHttp {
    void createUser(@NotBlank String username, @NotBlank String password) throws EntityAlreadyExistsException;
}
