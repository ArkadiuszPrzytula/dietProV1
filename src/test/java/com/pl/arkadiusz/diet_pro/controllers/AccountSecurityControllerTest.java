package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;

import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.pl.arkadiusz.diet_pro.utils.TestUtil.ID;
import static com.pl.arkadiusz.diet_pro.utils.TestUtil.TOKEN;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
 public class AccountSecurityControllerTest {
    MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Captor
    private ArgumentCaptor<String> tokenCaptor;

    @MockBean
    private UserService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void confirm_registration_should_return_link_to_user_id() throws Exception {
        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();
        verificationTokenDTO.setToken(TOKEN);
        verificationTokenDTO.setUserId(ID);
        when(userService.getVerificationToken(verificationTokenDTO.getToken())).thenReturn(verificationTokenDTO);

        when(userService.checkTokenExpireTime(verificationTokenDTO)).thenReturn(true);
        when(userService.verifyUser(verificationTokenDTO.getUserId())).thenReturn(ID);


        mvc.perform(get("/account/confirm.html?token=" + TOKEN)
                .contentType("application/json")
                .accept("application/json")
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect((header().string(HttpHeaders.LOCATION, "http://localhost/user/" + ID)));
    }

    @Test
    public void confirm_registration_should_throws_error_when_token_expire() throws Exception {
        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();
        verificationTokenDTO.setToken(TOKEN);
        verificationTokenDTO.setUserId(ID);

        when(userService.getVerificationToken(tokenCaptor.capture())).thenReturn(verificationTokenDTO);
        when(userService.checkTokenExpireTime(verificationTokenDTO)).thenThrow(new TokenExpiredException());
        when(userService.verifyUser(verificationTokenDTO.getUserId())).thenReturn(ID);

        mvc.perform(get("/account/confirm.html?token=" + TOKEN)
                .contentType("application/json")
                .accept("application/json")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/account/confirm.html"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Sorry! Your token has expired"))
                .andExpect(jsonPath("$.error").value(TokenExpiredException.class.getSimpleName()));
    }


    @Test
    public void confirm_registration_should_throws_error_when_invalid_token() throws Exception {
        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();
        verificationTokenDTO.setToken(TOKEN);
        verificationTokenDTO.setUserId(ID);

        when(userService.getVerificationToken(tokenCaptor.capture())).thenThrow(new InvalidTokenException());
        when(userService.checkTokenExpireTime(verificationTokenDTO)).thenReturn(true);
        when(userService.verifyUser(verificationTokenDTO.getUserId())).thenReturn(ID);

        mvc.perform(get("/account/confirm.html?token=" + TOKEN)
                .contentType("application/json")
                .accept("application/json")
        )

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/account/confirm.html"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("invalid token"))
                .andExpect(jsonPath("$.error").value(InvalidTokenException.class.getSimpleName()));
    }

}