package de.hsos.roomplanner.user.control;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import de.hsos.roomplanner.user.control.dto.UserDto;
import de.hsos.roomplanner.user.entity.User;
import de.hsos.roomplanner.user.repository.UserRepository;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import io.quarkus.elytron.security.common.BcryptUtil;

/**
 * @author Benno Steinkamp
 */
@RequestScoped
public class UserService implements UserServiceHttp, UserServiceRest {

    @Inject
    UserRepository userRepository;

    
    /** 
     * @param user - The user to create
     * @throws EntityAlreadyExistsException If a user with that username already exists
     */
    @Transactional
    @Override
    public void createUser(@Valid UserDto user) throws EntityAlreadyExistsException {
        this.createUser(user.getUsername(), user.getPassword());
    }

    
    /** 
     * @param username - The username of the user to create
     * @param password - The password of the user to create
     * @throws EntityAlreadyExistsException If a user with that username already exists
     */
    @Transactional
    @Override
    public void createUser(@NotBlank String username, @NotBlank String password) throws EntityAlreadyExistsException {

        try {
            userRepository.find(username);
            throw new EntityAlreadyExistsException(User.class, username);
        } catch (EntityDoesNotExistException ex) {
        }

        String hashedPassword = BcryptUtil.bcryptHash(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.getRoles().add("user");

        userRepository.persist(newUser);

    }

}
