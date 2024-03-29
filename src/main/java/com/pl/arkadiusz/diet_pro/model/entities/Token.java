package com.pl.arkadiusz.diet_pro.model.entities;


import lombok.*;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Setter
@Getter

@ToString(callSuper = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "verification_token")
public class Token {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public Token(User user, String token) {
        this.user = user;
        this.token = token;
        this.expiryDate = calculatedExpiryDate();
    }

    private Date calculatedExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, EXPIRATION);
        return new Date(calendar.getTime().getTime());
    }
}


