package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserStoreTests {

    private User john;
    private User sam;
    private UserEntity johnEntity;
    private UserEntity samEntity;
    private FakeUserJpaRepository fakeUserJpaRepository;
    private UserJpaStore userJpaStore;

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

        johnEntity = new UserEntity(john);
        samEntity = new UserEntity(sam);
        fakeUserJpaRepository = new FakeUserJpaRepository();
        userJpaStore = new UserJpaStore(fakeUserJpaRepository);
    }

    @Test
    void tdd_for_getAllUser_returnListUsers() throws Exception {
        // given
        fakeUserJpaRepository.save(johnEntity);
        fakeUserJpaRepository.save(samEntity);
        // when
        List<User> allUsers = userJpaStore.getAllUsers();

        // then
        assertThat(allUsers.size()).isEqualTo(2);
        List<String> usernames = allUsers.stream().map(User::getName).collect(Collectors.toList());
        assertThat(usernames.contains("john")).isTrue();
        assertThat(usernames.contains("sam")).isTrue();
    }

    @Test
    void tdd_for_getUserWithId_returnsUser() throws Exception {
        // given
        fakeUserJpaRepository.save(johnEntity);

        // when
        User findUser = userJpaStore.getUser(john.getId());

        // then
        assertThat(findUser.getName()).isEqualTo("john");
        assertThat(findUser.getUsername()).isEqualTo("john@email.com");
        assertThat(findUser.getPhone()).isEqualTo("1234-1234");
        assertThat(findUser.getPassword()).isEqualTo("1234");
        assertThat(findUser.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_getUserWithIncorrectId_throwsException() throws Exception {
        // then
        assertThatThrownBy(() -> userJpaStore.getUser("999"))
                .isInstanceOf(NoSuchUserException.class);
    }

    @Test
    void tdd_for_addUser_returnsUser() throws Exception {
        // given

        // when
        User user = userJpaStore.addUser(john);

        // then
        assertThat(user.getName()).isEqualTo("john");
        assertThat(user.getUsername()).isEqualTo("john@email.com");
        assertThat(user.getPhone()).isEqualTo("1234-1234");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_addedUser_can_getUser() throws Exception {
        // given
        userJpaStore.addUser(john);

        // when
        User user = userJpaStore.getUser(john.getId());

        // then
        assertThat(user.getName()).isEqualTo("john");
        assertThat(user.getUsername()).isEqualTo("john@email.com");
        assertThat(user.getPhone()).isEqualTo("1234-1234");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_updateUser_returnsUser() throws Exception {
        // given
        fakeUserJpaRepository.save(johnEntity);
        assertThat(john.getPassword()).isEqualTo("1234");

        NameValueList nameValueList = new NameValueList();
        nameValueList.add(new NameValue("password", "4321"));
        john.setValues(nameValueList);

        // when
        User user = userJpaStore.updateUser(john);


        // then
        assertThat(user.getName()).isEqualTo("john");
        assertThat(user.getUsername()).isEqualTo("john@email.com");
        assertThat(user.getPhone()).isEqualTo("1234-1234");
        assertThat(user.getPassword()).isEqualTo("4321");
        assertThat(user.getRoles().get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void tdd_for_deleteUser_deletesUser() throws Exception {
        // given
        fakeUserJpaRepository.save(johnEntity);
        fakeUserJpaRepository.save(samEntity);

        // when
        userJpaStore.deleteUser(john.getId());

        // then
        List<UserEntity> users = new ArrayList<>(fakeUserJpaRepository.getUserStore().values());
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("sam");

    }
}
