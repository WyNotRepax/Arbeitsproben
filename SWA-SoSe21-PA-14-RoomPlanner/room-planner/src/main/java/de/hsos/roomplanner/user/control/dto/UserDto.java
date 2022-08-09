package de.hsos.roomplanner.user.control.dto;

import javax.validation.constraints.NotBlank;

/**
 * @author Christopg Freimuth
 */

public class UserDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

}
