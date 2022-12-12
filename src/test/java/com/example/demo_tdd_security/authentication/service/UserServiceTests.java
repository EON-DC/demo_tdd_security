package com.example.demo_tdd_security.authentication.service;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import com.example.demo_tdd_security.authentication.store.FakeUserJpaRepository;
import com.example.demo_tdd_security.authentication.store.UserJpaStore;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTests {

    private User john;
    private User sam;
    private UserJpaStore userJpaStore;
    private DefaultUserService userService;

    @BeforeEach
    void setUp() {
        john = User.builder()
                .name("john")
                .email("john@email.com")
                .password("1234")
                .roles(Collections.singletonList(UserRole.ROLE_USER))
                .phone("1234-1234")
                .build();
        sam = User.builder()
                .name("sam")
                .email("sam@email.com")
                .roles(Arrays.asList(new UserRole[]{
                        UserRole.ROLE_USER, UserRole.ROLE_ADMIN}))
                .build();

        userJpaStore = new UserJpaStore(new FakeUserJpaRepository());
        userService = new DefaultUserService(userJpaStore);
    }

    @Test
    void tdd_for_getAllUser_returnsListUsers() throws Exception {
        // given
        userJpaStore.addUser(john);
        userJpaStore.addUser(sam);

        // when
        List<User> allUsers = userService.getAllUsers();

        // then
        assertThat(allUsers.size()).isEqualTo(2);
        List<String> usernames = allUsers.stream().map(User::getName).collect(Collectors.toList());
        assertThat(usernames.contains("john")).isTrue();
        assertThat(usernames.contains("sam")).isTrue();
    }

    @Test
    void tdd_for_getUser_returnsUser() throws Exception {
        // given
        userJpaStore.addUser(john);
        // when
        User findUser = userService.get(john.getId());

        // then
        assertThat(findUser.getName()).isEqualTo("john");
        assertThat(findUser.getUsername()).isEqualTo("john@email.com");
        assertThat(findUser.getPhone()).isEqualTo("1234-1234");
        assertThat(findUser.getPassword()).isEqualTo("1234");
        assertThat(findUser.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_getUserWithWrongId_throwsException() throws Exception {
        assertThatThrownBy(() -> userService.get("999"))
                .isInstanceOf(NoSuchUserException.class);
    }

    @Test
    void tdd_for_addUser_returnsUser() throws Exception {
        // when
        User addedUser = userService.add(john);

        // then
        assertThat(addedUser.getName()).isEqualTo("john");
        assertThat(addedUser.getUsername()).isEqualTo("john@email.com");
        assertThat(addedUser.getPhone()).isEqualTo("1234-1234");
        assertThat(addedUser.getPassword()).isEqualTo("1234");
        assertThat(addedUser.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_updateUser_returnsUpdatedUser() throws Exception{
        // given
        userJpaStore.addUser(john);
        assertThat(john.getPassword()).isEqualTo("1234");

        // when
        NameValueList nameValueList = new NameValueList();
        nameValueList.add(new NameValue("password", "4321"));

        User user = userService.update(john.getId(), nameValueList);

        // then
        assertThat(user.getName()).isEqualTo("john");
        assertThat(user.getUsername()).isEqualTo("john@email.com");
        assertThat(user.getPhone()).isEqualTo("1234-1234");
        assertThat(user.getPassword()).isEqualTo("4321");
        assertThat(user.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_deleteUser_deleteUser() throws Exception{
        // given
        userJpaStore.addUser(john);

        // when
        userService.delete(john.getId());

        // then
        assertThat(userService.getAllUsers().size()).isEqualTo(0);
    }


}
