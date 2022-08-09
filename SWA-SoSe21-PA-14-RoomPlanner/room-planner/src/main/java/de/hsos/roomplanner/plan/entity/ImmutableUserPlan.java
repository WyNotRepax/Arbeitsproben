package de.hsos.roomplanner.plan.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * @author Benno Steinkamp
 */

@Entity
@Table(name = "User")
@NamedQuery(name = "ImmutableUserPlan.find", query = "SELECT u FROM ImmutableUserPlan u WHERE u.username = :username")
@Immutable
public class ImmutableUserPlan {

    @Id
    private String username;

    public ImmutableUserPlan() {}

    public String getUsername() { return username; }

}

