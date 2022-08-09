package de.hsos.roomplanner.furniture.entity;

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
@NamedQuery(name = "ImmutableUserFurniture.find", query = "SELECT u FROM ImmutableUserFurniture u WHERE u.username = :username")
@Immutable
public class ImmutableUserFurniture {

    @Id
    private String username;

    public ImmutableUserFurniture() {}

    public String getUsername() { return username; }

}
