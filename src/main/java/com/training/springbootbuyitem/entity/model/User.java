package com.training.springbootbuyitem.entity.model;

import com.training.springbootbuyitem.enums.EnumUserState;
import com.training.springbootbuyitem.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;

@Proxy(lazy = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;
    private String name;
    private int age;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private EnumUserState state;

    private Instant dateOfBirth;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles;

    private String username;


    public User(String name, String username) {
        this.name = name;
        this.age = -1;
        this.username = username;
    }

    public void setUsername(String username) { this.username = username; }
}
