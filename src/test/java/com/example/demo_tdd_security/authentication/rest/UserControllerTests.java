package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import com.example.demo_tdd_security.share.json.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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

    private MockMvc mockMvc;
    private UserStubService userStubService;
    private UserSpyService userSpyService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        userStubService = new UserStubService();
        userSpyService = new UserSpyService();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userStubService))
                .build();
    }

    @Test
    void test_getAll_returnsOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void test_getAll_returnsList() throws Exception {
        userStubService.setGetAll_returnValue(
                Arrays.asList(new User[]{
                        User.builder().name("john").build(),
                        User.builder().name("sam").build()}
                ));

        String result = mockMvc.perform(get("/users"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> users = JsonUtils.fromJsonList(result, User.class);
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void test_getUser_returnsOk() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void test_getUser_returnsUser() throws Exception {
        userStubService.setGet_returnValue(User.builder().id("1").name("john").build());

        String userAsString = mockMvc.perform(get("/users/1"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        User returnUser = JsonUtils.fromJson(userAsString, User.class);
        assertThat(returnUser.getName()).isEqualTo("john");
    }

    @Test
    void test_getUser_usesCorrectParameter() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userSpyService))
                .build();

        mockMvc.perform(get("/users/999"));

        assertThat(userSpyService.getGet_argument_id()).isEqualTo("999");
    }

    @Test
    void test_addUser_returnsOk() throws Exception {
        userStubService.setAdd_returnValue(User.builder().name("john").build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(jsonPath("$.name", equalTo("john")));

    }

    @Test
    void test_addUser_usesCorrectParameter() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userSpyService))
                .build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(User.builder().name("john").build())));

        User add_argument_body = userSpyService.getAdd_argument_body();
        assertThat(add_argument_body.getName()).isEqualTo("john");
    }

    @Test
    void test_updateUser_returnsOk() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(User.builder().name("john").build())))
                .andExpect(status().isOk());
    }

    @Test
    void test_updateUser_returnsUser() throws Exception {
        userStubService.setUpdate_returnValue(User.builder().name("john").email("john@email.com").build());

        mockMvc.perform(patch("/users/1")
                        .content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", equalTo("john")))
                .andExpect(jsonPath("$.email", equalTo("john@email.com")));
    }

    @Test
    void test_updateUser_usesCorrectArgument() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userSpyService))
                .build();
        NameValueList nameValueList = new NameValueList();
        nameValueList.add(new NameValue("phone", "010-1234-4321"));

        mockMvc.perform(patch("/users/1")
                .content(mapper.writeValueAsString(nameValueList))
                .contentType(MediaType.APPLICATION_JSON));

        String update_argument_id = userSpyService.getUpdate_argument_id();
        NameValue argumentNameValue = userSpyService.getUpdate_argument_body().getNameValues().get(0);
        assertThat(update_argument_id).isEqualTo("1");
        assertThat(argumentNameValue.getName()).isEqualTo("phone");
        assertThat(argumentNameValue.getValue()).isEqualTo("010-1234-4321");
    }

    @Test
    void test_deleteUser_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

    }

    @Test
    void test_deleteUser_usesCorrectParameter() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userSpyService))
                .build();

        mockMvc.perform(delete("/users/1"));

        assertThat(userSpyService.getDelete_argument_id()).isEqualTo("1");
    }
}
