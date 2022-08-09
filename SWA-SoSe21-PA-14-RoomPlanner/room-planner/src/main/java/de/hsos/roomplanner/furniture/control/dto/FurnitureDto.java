package de.hsos.roomplanner.furniture.control.dto;

/**
 * @author Benno Steinkamp
 */
public class FurnitureDto extends FurnitureDtoCreateUpdate {

    private long id;

    public FurnitureDto() { super(); }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

}
