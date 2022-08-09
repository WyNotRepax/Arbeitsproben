package de.hsos.roomplanner.util.color;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a RGB html color as a string of hex rgb values. 
 * 
 * @author Benno Steinkamp
 */
@Embeddable
public class Color {

    @JsonValue
    @NotBlank
    @ColorString
    private String value;

    public Color(String value) { this.value = value.toLowerCase(); }

    public Color() { this("#000000"); }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value.toLowerCase(); }

    @Override
    public String toString() { return this.value; }

}
