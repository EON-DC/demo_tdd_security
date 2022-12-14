package com.example.demo_tdd_security.authentication.rest;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.share.json.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        System.out.println("new User[]{john} = " + JsonUtil.toJson(new User[]{john}));

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


}
