package de.hsos.roomplanner.furniture.gateway;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.hsos.roomplanner.furniture.entity.ImmutableUserFurniture;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;

/**
 * @author Benno Steinkamp
 */
@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager em;

    public ImmutableUserFurniture findUser(String username) throws EntityDoesNotExistException {
        TypedQuery<ImmutableUserFurniture> userQuery = em
                .createNamedQuery("ImmutableUserFurniture.find", ImmutableUserFurniture.class);
        userQuery.setParameter("username", username);
        try {
            return userQuery.getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityDoesNotExistException(ImmutableUserFurniture.class, username);
        }
    }

}
