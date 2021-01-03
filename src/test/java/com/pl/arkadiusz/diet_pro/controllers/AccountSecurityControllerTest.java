package com.pl.arkadiusz.diet_pro.controllers;

import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;

import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.services.SendMailToUserService;
import com.pl.arkadiusz.diet_pro.services.UserAccountService;
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

import static com.pl.arkadiusz.diet_pro.utils.TestUtil.*;
import static org.mockito.Mockito.*;
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
    private UserAccountService userAccountService;
    @MockBean
    private UserService userService;

    @MockBean
    private SendMailToUserService sendMailToUserService;


    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void confirm_registration_should_return_link_to_user_id() throws Exception {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(TOKEN);
        tokenDTO.setUserId(ID);
        when(userAccountService.getToken(tokenDTO.getToken())).thenReturn(tokenDTO);

        when(userAccountService.checkTokenExpireTime(tokenDTO)).thenReturn(true);
        when(userAccountService.verifyUser(tokenDTO.getUserId())).thenReturn(ID);


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
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(TOKEN);
        tokenDTO.setUserId(ID);

        when(userAccountService.getToken(tokenCaptor.capture())).thenReturn(tokenDTO);
        when(userAccountService.checkTokenExpireTime(tokenDTO)).thenThrow(new TokenExpiredException());
        when(userAccountService.verifyUser(tokenDTO.getUserId())).thenReturn(ID);

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
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(TOKEN);
        tokenDTO.setUserId(ID);

        when(userAccountService.getToken(tokenCaptor.capture())).thenThrow(new InvalidTokenException());
        when(userAccountService.checkTokenExpireTime(tokenDTO)).thenReturn(true);
        when(userAccountService.verifyUser(tokenDTO.getUserId())).thenReturn(ID);

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

    @Test
    public void should_send_user_verification_token_when_user_id_exist_in_database_and_user_is_not_enabled() {
        //given
        UserPlainDto userPlainDto = ;

        //when
        when(userService.getUserPlainDto(userPlainDto.getId()))


    }

private UserPlainDto createUserPlainDTOForTest(Long){
    UserPlainDto userPlainDto = new UserPlainDto();
    userPlainDto.setId();
    userPlainDto.isEnable()
    userPlainDto.setEmail();
    userPlainDto.setActive();
    userPlainDto.setGraduatesName();
    userPlainDto.setUsername();
}

}