package com.pl.arkadiusz.diet_pro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.userDto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.dto.userDto.TokenDTO;

import com.pl.arkadiusz.diet_pro.model.entities.enums.TokenType;
import com.pl.arkadiusz.diet_pro.services.RegistrationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class RegistrationControllerTest {

    MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Captor
    private ArgumentCaptor<UserRegisterDTO> userRegisterDTOArgumentCaptor;


    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    RegistrationService registrationService;

    @MockBean
    UserAccountService userAccountService;

    @MockBean
    SendMailToUserService sendMailToUserService;


    @MockBean
    UserService userService;


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

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(3L);
        tokenDTO.setToken(TOKEN);
        //when
        when(registrationService.register(userRegisterDTOArgumentCaptor.capture())).thenReturn(userPlainDtoServiceResponse);
        when(userAccountService.createTokenUsername(userPlainDtoServiceResponse.getUsername())).thenReturn(TOKEN);

        //then
        mvc.perform(post("/registration")
                .contentType("application/json")
                .accept("application/json")
                .content(objectMapper.writeValueAsString(registeredUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect((header().string(HttpHeaders.LOCATION, "http://localhost/user/" + ID)));

        verify(sendMailToUserService, times(1))
                .sendTokenToUserFactory("", userPlainDtoServiceResponse,
                tokenDTO.getToken(), TokenType.REGISTRATION_VERIFY);

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