package de.hsos.roomplanner.user.control;

import javax.validation.Valid;

import de.hsos.roomplanner.user.control.dto.UserDto;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;

/**
 * @author Christoph Freimuth
 */

public interface UserServiceRest {

    void createUser(@Valid UserDto user) throws EntityAlreadyExistsException;

}
