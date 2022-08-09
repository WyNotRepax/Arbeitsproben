package de.hsos.roomplanner.user.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotBlank;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.PasswordType;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

/**
 * @author Benno Steinkamp
 * @author Christoph Freimuth
 */

@Entity
@UserDefinition
@NamedQuery(name = "User.find", query = "SELECT u FROM User u WHERE u.username = :username")
public class User {

    @Username
    @Id
    @NotBlank
    private String username;
    @NotBlank
    @Password(value = PasswordType.MCF)
    private String password;
    @Roles
    @ElementCollection
    private Set<String> roles;

    public User() { this.roles = new HashSet<>(); }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Set<String> getRoles() { return roles; }

    public void setRoles(Set<String> roles) { this.roles = roles; }

}