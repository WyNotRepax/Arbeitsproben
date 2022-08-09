package de.hsos.roomplanner.furniture.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.color.Color;

/**
 * @author Christoph Freimuth
 * @author Benno Steinkamp
 */
@Entity
@NamedQuery(name = "Furniture.findAll", query = "SELECT f FROM Furniture f WHERE f.name LIKE :filterName AND f.user = :user")
@NamedQuery(name = "Furniture.findAll_COUNT", query = "SELECT count(f) FROM Furniture f WHERE f.name LIKE :filterName AND f.user = :user")
@NamedQuery(name = "Furniture.find", query = "SELECT f FROM Furniture f WHERE f.id = :id AND f.user = :user")
public class Furniture {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String name;

    @Embedded
    @Valid
    private Dimension dimension;

    @Valid
    @Embedded
    private Color color;

    @ManyToOne
    private ImmutableUserFurniture user;

    public Furniture() {}

    public Furniture(String name, Dimension dimension, Color color) {
        this.name = name;
        this.dimension = dimension;
        this.color = color;
    }

    public long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Dimension getDimension() { return dimension; }

    public void setDimension(Dimension dimension) { this.dimension = dimension; }

    public Color getColor() { return color; }

    public void setColor(Color color) { this.color = color; }

    public ImmutableUserFurniture getUser() { return user; }

    public void setUser(ImmutableUserFurniture user) { this.user = user; }

}
