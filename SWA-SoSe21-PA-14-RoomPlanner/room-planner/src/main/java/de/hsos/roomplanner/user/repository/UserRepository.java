package de.hsos.roomplanner.user.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.hsos.roomplanner.user.entity.User;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;

/**
 * @author Benno Steinkamp
 */
@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager em;

    
    /** 
     * @param user - The User object to perisist
     * @throws EntityAlreadyExistsException If a user with that name already exists
     */
    public void persist(User user) throws EntityAlreadyExistsException {
        try {
            em.persist(user);
        } catch (EntityExistsException ex) {
            throw new EntityAlreadyExistsException(User.class, user.getUsername());
        }
    }

    
    /** 
     * @param username - The username of the User object to find
     * @return User - The user object found
     * @throws EntityDoesNotExistException If no user with that name could be found
     */
    public User find(String username) throws EntityDoesNotExistException {
        TypedQuery<User> userQuery = em.createNamedQuery("User.find", User.class);
        userQuery.setParameter("username", username);
        try {
            return userQuery.getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityDoesNotExistException(User.class, username);
        }
    }

}
