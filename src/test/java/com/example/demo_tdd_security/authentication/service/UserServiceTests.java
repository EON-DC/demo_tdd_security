package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import com.example.demo_tdd_security.authentication.store.FakeUserJpaRepository;
import com.example.demo_tdd_security.authentication.store.UserJpaStore;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTests {

    private User john;
    private User sam;
    private UserJpaStore userStore;
    private DefaultUserService defaultUserService;

    @BeforeEach
    void setUp() {
        john = User.builder().name("john").build();
        sam = User.builder().name("sam").build();

        userStore = new UserJpaStore(new FakeUserJpaRepository());
        defaultUserService = new DefaultUserService(userStore);
    }

    @Test
    void test_getAllUsers_returnUserList() {
        userStore.add(john);
        userStore.add(sam);

        List<User> users = defaultUserService.getAll();

        assertThat(users.size()).isEqualTo(2);
        List<String> usernames = users.stream().map(User::getName).collect(Collectors.toList());
        assertThat(usernames.contains("john")).isTrue();
        assertThat(usernames.contains("sam")).isTrue();
    }

    @Test
    void test_getUser_returnsUser() {
        userStore.add(john);
        User getUser = defaultUserService.get(john.getId());
        assertThat(getUser.getId()).isEqualTo(john.getId());
    }

    @Test
    void test_updateUser_returnsUser() {
        userStore.add(john);
        NameValueList nameValueList = new NameValueList();
        nameValueList.add(new NameValue("phone", "010-1234-1234"));

        User updatedUser = defaultUserService.update(john.getId(), nameValueList);
        assertThat(updatedUser.getPhone()).isEqualTo("010-1234-1234");
    }

    @Test
    void test_getUserWhenEmpty_throwsException() {
        assertThatThrownBy(() -> defaultUserService.get("000")).isInstanceOf(NoSuchUserException.class)
                .hasMessage("No such user id : 000");
    }

    @Test
    void test_addUser_returnsUsers() {
        User addedUser = defaultUserService.add(john);
        assertThat(addedUser.getName()).isEqualTo("john");
    }

    @Test
    void test_updatedUserWhenEmpty_throwsException() {
        assertThatThrownBy(() -> defaultUserService.update("999", new NameValueList()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void test_deleteUser_deletesUser() {
        userStore.add(john);
        defaultUserService.delete(john.getId());
        assertThat(defaultUserService.getAll().isEmpty()).isTrue();
    }

    @Test
    void test_deleteUserWhenEmpty_throwsException() {
        assertThatThrownBy(() -> defaultUserService.delete("000"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No such user id : 000");

    }

    @Test
    void test_loadUserByUsername_returnUserWithCorrectEmail() {
        userStore.add(User.builder().email("email").build());
        User user = defaultUserService.loadUserByUsername("email");

        assertThat(user.getEmail()).isEqualTo("email");

    }
    @Test
    void test_loadUserByUsernameWhenEmpty_throws_Exception() {
        assertThatThrownBy(() -> defaultUserService.loadUserByUsername("000"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid Email or password...");
    }
}
