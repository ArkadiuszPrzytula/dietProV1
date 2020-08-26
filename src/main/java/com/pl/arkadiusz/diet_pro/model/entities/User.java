package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class User extends EntityBase implements UserDetails {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name ="password", nullable = false)
    private String password;

    @Column(name = "account_non_expired" )
    private boolean accountNonExpired;

    @Column(name = "account_non_locked" )
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired" )
    private boolean credentialsNonExpired;

    @Column(name = "enabled" )
    private boolean enabled;

    // Relations part //

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_data_id", referencedColumnName = "id")
    private PersonalData personalData;

    @Column (name = "emails", nullable = false)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
