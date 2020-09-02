package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor

public class EntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "active")
    private boolean active;

    @PrePersist
    private void prePersist() {
        createdOn = LocalDateTime.now();
        updatedOn = null;
    }

    @PreUpdate
    private void perUpdate() {
        updatedOn = LocalDateTime.now();
    }

}
