package de.hsos.roomplanner.plan.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.Valid;

import de.hsos.roomplanner.util.Position;
import io.smallrye.common.constraint.NotNull;

/**
 * @author Christoph Freimuth
 * @author Benno Steinkamp
 */
@Entity
@NamedQuery(name="FurnitureInPlan.find",query = "SELECT f FROM FurnitureInPlan f WHERE f.id = :id AND f.plan = :plan")
public class FurnitureInPlan {

    @Id
    @GeneratedValue
    private long id;

    @Embedded
    @Valid
    @NotNull
    private Position position;

    @ManyToOne
    private ImmutableFurniture furniture;

    @ManyToOne
    private Plan plan;

    public FurnitureInPlan() {}

    public FurnitureInPlan(Position position, ImmutableFurniture furniture, Plan plan) {
        this.position = position;
        this.furniture = furniture;
        this.plan = plan;
    }

    public long getId() { return this.id; }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

    public ImmutableFurniture getFurniture() { return furniture; }

    public void setFurniture(ImmutableFurniture furniture) { this.furniture = furniture; }

    public Plan getPlan() { return plan; }

    public void setPlan(Plan plan) { this.plan = plan; }

}
