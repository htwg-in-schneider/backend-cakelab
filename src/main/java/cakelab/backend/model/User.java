package cakelab.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user") // <-- Rename table to avoid reserved keyword
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String oauthId;

    @Enumerated(EnumType.STRING) // <-- Use JPA enum mapping
    private Role role;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

 
    public String getOauthId() {
        return oauthId;
    }
    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}