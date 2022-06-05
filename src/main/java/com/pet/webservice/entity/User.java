package com.pet.webservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pet.webservice.entity.enums.ERole;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/* create models and dependencies of users, posts, comments and content (image model) between each other */
/* also create dependencies between server and MySQL DB */
/* create all getters, setters and entities */

@Entity
@Data
public class User implements UserDetails { // implements UserDetails from Spring Security
    /* for unique generating id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /* can't be empty */
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true, updatable = false)
    private String username;
    @Column(length = 64)
    private String password;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private String bio;

    /* create dependence between users and roles */
    @ElementCollection(targetClass = ERole.class)
    /* create a new table in db */
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> role = new HashSet<>();

    /* create dependence between users and posts */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    /* Security methods */

    public User() {
    }

    public User(Long id,
                String username,
                String password,
                String email,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
