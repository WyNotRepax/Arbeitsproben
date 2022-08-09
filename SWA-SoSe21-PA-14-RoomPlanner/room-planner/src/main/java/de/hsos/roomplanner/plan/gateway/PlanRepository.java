package de.hsos.roomplanner.plan.gateway;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.hsos.roomplanner.plan.entity.ImmutableUserPlan;
import de.hsos.roomplanner.plan.entity.Plan;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;

/**
 * @author Benno Steinkamp
 */
@ApplicationScoped
public class PlanRepository {

    @Inject
    EntityManager em;

    /**
     * @param Plan - the plan to persist.
     */
    public void persist(Plan plan) { em.persist(plan); }

    /**
     * @param filterName     - the Name to filter by. This generates the SQL
     *                       expression {@code %<filterName>%}.
     * @param filterDateFrom - start date to filter by.
     * @param filterDateTo   - end date to filter by.
     * @param pageSize       - the size of the page to be returned.
     * @param pageIndex      - the 0 starting index of the page of results to be
     *                       returned.
     */
    public Page<Plan> findAll(
            ImmutableUserPlan user,
            String filterName,
            LocalDate filterDateFrom,
            LocalDate filterDateTo,
            int pageSize,
            int pageIndex
    ) {
        TypedQuery<Plan> planQuery = em.createNamedQuery("Plan.findAll", Plan.class);
        planQuery.setParameter("user", user);
        planQuery.setParameter("filterName", "%" + filterName + "%");
        planQuery.setParameter("filterDateFrom", filterDateFrom);
        planQuery.setParameter("filterDateTo", filterDateTo);
        planQuery.setMaxResults(pageSize);
        planQuery.setFirstResult(pageSize * pageIndex);

        TypedQuery<Long> planCountQuery = em.createNamedQuery("Plan.findAll_COUNT", Long.class);
        planCountQuery.setParameter("user", user);
        planCountQuery.setParameter("filterName", "%" + filterName + "%");
        planCountQuery.setParameter("filterDateFrom", filterDateFrom);
        planCountQuery.setParameter("filterDateTo", filterDateTo);

        return new Page<Plan>(planCountQuery.getSingleResult(), planQuery.getResultList());
    }

    /**
     * @param id - the ID of the plan to find.
     * @return the plan found, or null if not found.
     */
    public Plan find(ImmutableUserPlan user, long id) throws EntityDoesNotExistException {
        TypedQuery<Plan> planQuery = em.createNamedQuery("Plan.find", Plan.class);
        planQuery.setParameter("user", user);
        planQuery.setParameter("id", id);
        try {
            return planQuery.getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityDoesNotExistException(Plan.class, id);
        }
    }

    /**
     * @param id - the ID of the plan to be deleted.
     */
    public void delete(ImmutableUserPlan user, long id) throws EntityDoesNotExistException {
        Plan plan = this.find(user, id);
        em.remove(plan);
    }

}
