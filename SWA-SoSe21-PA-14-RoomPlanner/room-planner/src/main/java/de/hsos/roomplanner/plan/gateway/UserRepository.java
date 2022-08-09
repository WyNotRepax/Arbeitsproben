package de.hsos.roomplanner.plan.gateway;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.hsos.roomplanner.plan.entity.ImmutableUserPlan;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;

@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager em;

    public ImmutableUserPlan findUser(String username) throws EntityDoesNotExistException {
        TypedQuery<ImmutableUserPlan> userQuery = em
                .createNamedQuery("ImmutableUserPlan.find", ImmutableUserPlan.class);
        userQuery.setParameter("username", username);
        try {
            return userQuery.getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityDoesNotExistException(ImmutableUserPlan.class, username);
        }
    }

}
