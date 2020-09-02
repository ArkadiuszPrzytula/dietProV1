package com.pl.arkadiusz.diet_pro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pl.arkadiusz.diet_pro.dto.UserPlainDto;
import com.pl.arkadiusz.diet_pro.dto.UserRegisterDTO;
import com.pl.arkadiusz.diet_pro.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserControllerTest {

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
    @WithMockUser(username = "test", roles = {"ROLE_ADMIN"} ,authorities ={"READ_ALL"} )
    public void get_all_user() throws Exception {

        when(userService.getAllUser()).thenReturn(List.of(
                createUser("test1", "test@test1"),
                createUser("test2", "test@test2")
        ));

        this.mvc.perform(get("/user/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("test1"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].PersonalData").value(""))
                .andExpect(jsonPath("$[0].CreatedOn").value(createTime.toString()))
                .andExpect(jsonPath("$[0].UpdatedOn").value(""))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    private UserPlainDto createUser(String username, String email) {
        UserPlainDto user = new UserPlainDto();
        user.setId(++id);
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setActive(true);
        createTime = LocalDateTime.now();
        user.setCreatedOn(createTime);
        user.setUpdatedOn(null);
        return user;
    }


}