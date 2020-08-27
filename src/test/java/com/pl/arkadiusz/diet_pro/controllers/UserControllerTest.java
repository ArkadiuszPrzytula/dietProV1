package com.pl.arkadiusz.diet_pro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.model.entities.User;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest()
class UserControllerTest {

    MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Captor
    private ArgumentCaptor<UserRegisterDTO> userRegisterDTOArgumentCaptor;

    ObjectMapper objectMapper = new ObjectMapper();

    Long id = 0L;

    LocalDateTime createTime;

    @Test
    void get_all_user() throws Exception {
        when(userService.getAllUser()).thenReturn(List.of(
                createUser("test1", "test@test1"),
                createUser("test2", "test@test2")
        ));

        this.mvc.perform(get("/user/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
    .andExpect(jsonPath("$", hasSize(2)))
    .andExpect(jsonPath("$[0].id", is("1")))
    .andExpect(jsonPath("$[0].username", is("test1")))
    .andExpect(jsonPath("$[0].CredentialsNonExpired", is("true")))
    .andExpect(jsonPath("$[0].AccountNonLocked", is("true")))
    .andExpect(jsonPath("$[0].Enabled", is("true")))
    .andExpect(jsonPath("$[0].PersonalData", is("")))
    .andExpect(jsonPath("$[0].CreatedOn", is(createTime.toString())))
    .andExpect(jsonPath("$[0].UpdatedOn", is("")));
    }

    private User createUser(String username, String email) {
        User user = new User();
        user.setId(++id);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setPersonalData(null);
        user.setUsername(username);
        user.setEmail(email);
        createTime = LocalDateTime.now();
        user.setCreatedOn(createTime);
        user.setUpdatedOn(null);
        return user;
    }


}