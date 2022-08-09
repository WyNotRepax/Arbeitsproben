package de.hsos.roomplanner.plan.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * @author Christoph Freimuth
 * @author Benno Steinkamp
 */
@Entity
@Immutable
@Table(name = "Furniture")
@NamedQuery(name = "ImmutableFurniture.find", query = "SELECT f from ImmutableFurniture f WHERE f.id = :id AND f.user = :user")
public class ImmutableFurniture {

    @Id
    private long id;

    @ManyToOne
    private ImmutableUserPlan user;

    public ImmutableFurniture() {}

    public long getId() { return this.id; }

    
    public ImmutableUserPlan getUser() { return user; }

}
