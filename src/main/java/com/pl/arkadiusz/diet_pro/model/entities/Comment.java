package com.pl.arkadiusz.diet_pro.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Comment extends EntityBase {

    @Column(name = "contents", nullable = false)
    private String content;

    @Column(name ="edited")
    private boolean edited;

    // Relation part //

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_user_id")
    private User creatorUser;



}
