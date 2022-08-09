package de.hsos.roomplanner.plan.control.dto;

import java.time.LocalDate;

/**
 * @author Christoph Freimuth
 */
public class PlanDtoListing extends PlanDtoCreateUpdate {

    private long id;

    private LocalDate date;

    public PlanDtoListing() { super(); }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

}
