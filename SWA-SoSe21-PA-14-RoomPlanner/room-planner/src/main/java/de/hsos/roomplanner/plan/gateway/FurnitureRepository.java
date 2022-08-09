package de.hsos.roomplanner.plan.gateway;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.hsos.roomplanner.plan.entity.FurnitureInPlan;
import de.hsos.roomplanner.plan.entity.ImmutableFurniture;
import de.hsos.roomplanner.plan.entity.ImmutableUserPlan;
import de.hsos.roomplanner.plan.entity.Plan;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;

/**
 * @author Benno Steinkamp
 */

@ApplicationScoped
public class FurnitureRepository {

    @Inject
    EntityManager em;

    public ImmutableFurniture findFurniture(ImmutableUserPlan user, long id) throws EntityDoesNotExistException {
        TypedQuery<ImmutableFurniture> furnitureQuery = em
                .createNamedQuery("ImmutableFurniture.find", ImmutableFurniture.class);
        furnitureQuery.setParameter("id", id);
        furnitureQuery.setParameter("user", user);
        try {
            return furnitureQuery.getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityDoesNotExistException(ImmutableFurniture.class, id);
        }
    }

    public FurnitureInPlan findFurnitureInPlan(Plan plan, long id)
            throws EntityDoesNotExistException {
        TypedQuery<FurnitureInPlan> furnitureInPlanQuery = em
                .createNamedQuery("FurnitureInPlan.find", FurnitureInPlan.class);
        furnitureInPlanQuery.setParameter("id", id);
        furnitureInPlanQuery.setParameter("plan", plan);
        try {
            return furnitureInPlanQuery.getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityDoesNotExistException(FurnitureInPlan.class, id);
        }
    }

    public void persist(FurnitureInPlan furnitureInPlan) { em.persist(furnitureInPlan); }

    public void deleteFurnitureFromPlan(Plan plan, long furnitureInPlanId)
            throws EntityDoesNotExistException {
        FurnitureInPlan furnitureInPlan = this.findFurnitureInPlan(plan, furnitureInPlanId);
        this.em.remove(furnitureInPlan);
    }

}
