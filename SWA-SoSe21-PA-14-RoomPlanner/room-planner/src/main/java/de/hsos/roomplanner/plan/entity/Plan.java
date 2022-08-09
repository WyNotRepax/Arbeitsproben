package de.hsos.roomplanner.plan.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.date.DateMax;
import de.hsos.roomplanner.util.date.DateMin;

/**
 * @author Christoph Freimuth
 * @author Benno Steinkamp
 */

@Entity
@NamedQuery(name = "Plan.findAll", query = "SELECT p FROM Plan p WHERE p.name LIKE :filterName AND date >= :filterDateFrom AND date <= :filterDateTo AND p.user = :user")
@NamedQuery(name = "Plan.findAll_COUNT", query = "SELECT count(p) FROM Plan p WHERE p.name LIKE :filterName AND date >= :filterDateFrom AND date <= :filterDateTo AND p.user = :user")
@NamedQuery(name = "Plan.find", query = "SELECT p FROM Plan p WHERE p.id = :id AND p.user = :user")
@NamedQuery(name = "Plan.delete", query = "DELETE FROM Plan p WHERE p.id = :id AND p.user = :user")
public class Plan {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    @DateMax
    @DateMin
    private LocalDate date;
    private Dimension dimension;
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FurnitureInPlan> furnitureInPlan;
    @ManyToOne
    private ImmutableUserPlan user;

    public Plan() { furnitureInPlan = new ArrayList<FurnitureInPlan>(); }

    public Plan(String name, Dimension dimension) {
        this();
        this.name = name;
        this.dimension = dimension;
        this.date = LocalDate.now();
    }

    public long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }

    public Dimension getDimension() { return dimension; }

    public List<FurnitureInPlan> getFurnitureInPlan() { return furnitureInPlan; }

    public void setFurnitureInPlan(List<FurnitureInPlan> furnitureInPlan) { this.furnitureInPlan = furnitureInPlan; }

    public void setDimension(Dimension dimension) { this.dimension = dimension; }

    public ImmutableUserPlan getUser() { return user; }

    public void setUser(ImmutableUserPlan user) { this.user = user; }

}
