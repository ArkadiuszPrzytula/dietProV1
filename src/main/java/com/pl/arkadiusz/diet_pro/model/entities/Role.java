package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ConstructorBinding
@NoArgsConstructor
public class Role extends EntityBase {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private List<Privilege> privileges = new ArrayList<>();

    public Role(String name, List<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public enum RoleValue {
        ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER"), ROLE_MODERATOR("ROLE_MODERATOR");

        public String roleString;

        RoleValue(String roleString) {
            this.roleString = roleString;
        }
    }
}
