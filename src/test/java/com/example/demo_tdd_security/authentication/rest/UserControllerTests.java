package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import com.example.demo_tdd_security.share.json.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests {

    private User john;
    private MockMvc mockMvc;
    private UserStubService userStubService;
    private UserSpyService userSpyService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        john = User.builder()
                .name("john")
                .email("john@email.com")
                .password("1234")
                .roles(Arrays.asList(new UserRole[]{UserRole.ROLE_USER, UserRole.ROLE_ADMIN}))
                .phone("1234-1234")
                .build();
        userStubService = new UserStubService();
        userSpyService = new UserSpyService();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userStubService))
                .build();
    }

    @Test
    void tdd_for_getAll_returnsOk() throws Exception {
        // given
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void tdd_for_getAll_returnsList() throws Exception {
        // given
        userStubService.setGetAll_returnValue(
                Arrays.asList(new User[]{
                        john
                })
        );
        // when
        String result = mockMvc.perform(get("/users"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("result = " + result);

        List<User> userResult = JsonUtil.fromJsonList(result, User.class);

        // then
        assertThat(userResult.size()).isEqualTo(1);
    }

    @Test
    void tdd_for_getUser_returnUser() throws Exception {
        // given
        userStubService.setGet_returnValue(john);

        // when, then
        mockMvc.perform(get("/users/1"))
                .andExpect(jsonPath("$.name", equalTo("john")));
    }

    @Test
    void tdd_for_getUser_returnsOk() throws Exception {
        // when
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void tdd_for_getUser_usesCorrectParam() throws Exception {
        // given
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userSpyService))
                .build();

        // when
        mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        assertThat(userSpyService.getGet_argument_id()).isEqualTo("1");
    }

    @Test
    void tdd_for_updateUser_returnsOk() throws Exception {
        // given
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(new NameValueList())))
                .andExpect(status().isOk());
    }


    @Test
    void tdd_for_updateUser_returnsUser() throws Exception {
        // given
        userStubService.setUpdate_returnValue(john);

        // when
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(new NameValueList())))
                .andExpect(jsonPath("$.name", equalTo("john")));
    }

    @Test
    void tdd_for_updateUser_usesCorrectParam() throws Exception {
        // given
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userSpyService))
                .build();
        // when
        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new NameValueList(
                        Arrays.asList(new NameValue("name", "sam"))
                ))));

        // then
        assertThat(userSpyService.getUpdate_argument_id()).isEqualTo("1");
        NameValue nameValue = userSpyService.getUpdate_argument_list().getNameValues().get(0);
        assertThat(nameValue.getName()).isEqualTo("name");
        assertThat(nameValue.getValue()).isEqualTo("sam");
    }

    @Test
    void tdd_for_addUser_returnsOk() throws Exception{
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(john)))
                .andExpect(status().isOk());
    }

    @Test
    void tdd_for_addUser_returnsUser() throws Exception{
        // given
        userStubService.setAdd_returnValue(john);

        // when
        String contentAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(new User())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        User user = JsonUtil.fromJson(contentAsString, User.class);


        // then
        assertThat(user.getName()).isEqualTo("john");
    }

    @Test
    void tdd_for_deleteUser_returnsOk() throws Exception{
        // given
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }


    @Test
    void tdd_for_deleteUser_deleteUser() throws Exception{
        // given
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userSpyService))
                .build();

        // when
        mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        assertThat(userSpyService.getDelete_argument_id()).isEqualTo("1");


    }






}
