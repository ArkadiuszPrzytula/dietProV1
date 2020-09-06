package com.pl.arkadiusz.diet_pro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.dto.VerificationTokenDTO;
import com.pl.arkadiusz.diet_pro.errors.InvalidTokenException;
import com.pl.arkadiusz.diet_pro.errors.TokenExpiredException;
import com.pl.arkadiusz.diet_pro.services.RegistrationService;
import com.pl.arkadiusz.diet_pro.services.UserService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class RegistrationControllerTest {

    MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Captor
    private ArgumentCaptor<UserRegisterDTO> userRegisterDTOArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> tokenCaptor;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    RegistrationService registrationService;
    @MockBean
    UserService userService;

    final String USERNAME = "testUser";
    final Long ID = 3L;
    final String EMAIL = "test@test.PL";
    final String PASSWORD = "Testtest123";
    final String TOKEN = "24f377f3-06f3-4715-9c65-6b361f716c00";

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void it_should_return_created_user_location_by_id() throws Exception {
        //given
        UserRegisterDTO registeredUserDTO = getRequestRegisteredUserDTO(USERNAME, ID, EMAIL, PASSWORD, PASSWORD);
        UserPlainDto userPlainDtoServiceResponse = new UserPlainDto();
        userPlainDtoServiceResponse.setId(ID);
        userPlainDtoServiceResponse.setEmail(EMAIL);

        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();
        verificationTokenDTO.setUserId(3L);
        verificationTokenDTO.setToken(TOKEN);
        //when
        when(registrationService.register(userRegisterDTOArgumentCaptor.capture())).thenReturn(userPlainDtoServiceResponse);
        when(userService.createVerificationToken(userPlainDtoServiceResponse, TOKEN)).thenReturn(verificationTokenDTO);

        //then
        mvc.perform(post("/registration")
                .contentType("application/json")
                .accept("application/json")
                .content(objectMapper.writeValueAsString(registeredUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect((header().string(HttpHeaders.LOCATION, "http://localhost/user/" + ID)));
    }

    @Test
    public void it_should_return_code_400_wrong_rePassword() throws Exception {
        UserRegisterDTO registeredUserDTO = getRequestRegisteredUserDTO(USERNAME, ID, EMAIL, PASSWORD, "dumy");

        mvc.perform(post("/registration")
                .contentType("application/json")
                .accept("application/json")
                .content(objectMapper.writeValueAsString(registeredUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/registration"))
                .andExpect(jsonPath("$.errors.rePassword").exists());
    }

    @Test
    public void confirm_registration_should_return_link_to_user_id() throws Exception {
        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();
        verificationTokenDTO.setToken(TOKEN);
        verificationTokenDTO.setUserId(ID);
        when(userService.getVerificationToken(tokenCaptor.capture())).thenReturn(verificationTokenDTO);
        when(userService.checkTokenExpireTime(verificationTokenDTO)).thenReturn(true);
        when(userService.verifyUser(verificationTokenDTO.getUserId())).thenReturn(ID);

        mvc.perform(get("/registration/confirm.html?token=" + TOKEN)
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
        TokenExpiredException tokenExpiredException = new TokenExpiredException();
        System.out.println(tokenExpiredException.getLocalizedMessage());
        System.out.println(tokenExpiredException.getMessage());
        when(userService.getVerificationToken(tokenCaptor.capture())).thenReturn(verificationTokenDTO);
        when(userService.checkTokenExpireTime(verificationTokenDTO)).thenThrow(new TokenExpiredException());
        when(userService.verifyUser(verificationTokenDTO.getUserId())).thenReturn(ID);

        mvc.perform(get("/registration/confirm.html?token=" + TOKEN)
                .contentType("application/json")
                .accept("application/json")
        )

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/registration/confirm.html"))
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

        mvc.perform(get("/registration/confirm.html?token=" + TOKEN)
                .contentType("application/json")
                .accept("application/json")
        )

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/registration/confirm.html"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("invalid token"))
                .andExpect(jsonPath("$.error").value(InvalidTokenException.class.getSimpleName()));
    }

    @Test
    public void it_should_return_code_400_username_blank() throws Exception {
        UserRegisterDTO registeredUserDTO = getRequestRegisteredUserDTO("", ID, EMAIL, PASSWORD, PASSWORD);

        mvc.perform(post("/registration")
                .contentType("application/json")
                .accept("application/json")
                .content(objectMapper.writeValueAsString(registeredUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/registration"))
                .andExpect(jsonPath("$.errors.username").exists());
    }

    @Test
    public void it_should_return_code_400_email_blank() throws Exception {
        UserRegisterDTO registeredUserDTO = getRequestRegisteredUserDTO(USERNAME, ID, "", PASSWORD, PASSWORD);

        mvc.perform(post("/registration")
                .contentType("application/json")
                .accept("application/json")
                .content(objectMapper.writeValueAsString(registeredUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value("/registration"))
                .andExpect(jsonPath("$.errors.email").exists());
    }

    private UserRegisterDTO getRequestRegisteredUserDTO(String username, Long id, String email, String password, String rePassword) {
        UserRegisterDTO request = new UserRegisterDTO();
        request.setUsername(username);
        request.setPassword(password);
        request.setRePassword(rePassword);
        request.setEmail(email);
        return request;
    }

}