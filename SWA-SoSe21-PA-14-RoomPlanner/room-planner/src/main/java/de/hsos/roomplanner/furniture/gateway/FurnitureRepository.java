package de.hsos.roomplanner.furniture.gateway;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.hsos.roomplanner.furniture.entity.Furniture;
import de.hsos.roomplanner.furniture.entity.ImmutableUserFurniture;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;

/**
 * @author Benno Steinkamp
 */
@ApplicationScoped
public class FurnitureRepository {

    @Inject
    @PersistenceContext
    EntityManager em;

    /**
     * 
     * @param furniture - The furniture to persist.
     */
    public void persist(Furniture furniture) { em.persist(furniture); }

    /**
     * 
     * @param filterName - The name to filter by.
     * @param pageSize   - The size of the page to be returned.
     * @param pageIndex  - The 0 starting index of the page of results to be
     *                   returned.
     */
    public Page<Furniture> findAll(ImmutableUserFurniture user, String filterName, int pageSize, int pageIndex) {
        TypedQuery<Furniture> furnitureQuery = em.createNamedQuery("Furniture.findAll", Furniture.class)
                .setParameter("filterName", "%" + filterName + "%")
                .setParameter("user", user)
                .setMaxResults(pageSize)
                .setFirstResult(pageIndex * pageSize);
        TypedQuery<Long> furnitureCountQuery = em.createNamedQuery("Furniture.findAll_COUNT", Long.class)
                .setParameter("user", user)
                .setParameter("filterName", "%" + filterName + "%");
        return new Page<Furniture>(furnitureCountQuery.getSingleResult(), furnitureQuery.getResultList());
    }

    /**
     * 
     * @param id - The ID of the furniture to find
     * @return The furniture found
     * @throws EntityDoesNotExistException If there is no entity with the specified
     *                                     id
     */
    public Furniture find(ImmutableUserFurniture user, long id) throws EntityDoesNotExistException {
        TypedQuery<Furniture> furnitureQuery = em.createNamedQuery("Furniture.find", Furniture.class)
                .setParameter("user", user)
                .setParameter("id", id);
        try {
            return furnitureQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new EntityDoesNotExistException(Furniture.class, id);
        }
    }

    /**
     * @param id - this ID of the furniture to be deleted.
     * @throws EntityNotFoundException if no entity was deleted.
     */
    public void delete(ImmutableUserFurniture user, long id) throws EntityDoesNotExistException {
        Furniture furniture = this.find(user, id);
        em.remove(furniture);
    }

}
