package com.pl.arkadiusz.diet_pro.Listeners;

import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.model.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private UserPlainDto user;

    @Autowired
    private UserRepository userRepository;

    public OnRegistrationCompleteEvent(UserPlainDto user, Locale locale, String contextPath) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = contextPath;
    }
}
