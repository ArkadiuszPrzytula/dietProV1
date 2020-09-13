package com.pl.arkadiusz.diet_pro.model.entities;

import com.pl.arkadiusz.diet_pro.model.entities.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;



@ToString(callSuper = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "password_restart_token")
public class PasswordRestartToken {

    private static final int EXPIRATION = 20;

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

    public PasswordRestartToken(User user, String token) {
        this.token= token;
        this.user = user;
        this.expiryDate= calculatedExpiryDate();

    }

    private Date calculatedExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, EXPIRATION);
        return new Date(calendar.getTime().getTime());
    }
}
