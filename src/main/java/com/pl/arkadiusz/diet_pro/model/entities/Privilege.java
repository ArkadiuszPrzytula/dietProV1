package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "privileges")
@Getter
@Setter
@NoArgsConstructor
public class Privilege extends EntityBase {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }



    public  enum PrivilegeValue{
        USER_DELETE("USER_DELETE"),
        BAN_USER("BAN_USER"),
        READ_ALL("READ_ALL"),
        READ_CASUAL_ELEMENTS("READ_CASUAL_ELEMENTS"),
        MODIFY_ALL("MODIFY_ALL");;

        public String stringValue;

        PrivilegeValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }


}
