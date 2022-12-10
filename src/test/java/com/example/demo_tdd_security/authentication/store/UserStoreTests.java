package com.example.demo_tdd_security.authentication.store;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.exception.NoSuchUserException;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserStoreTests {

    private User john;
    private User sam;
    private UserEntity johnEntity;
    private UserEntity samEntity;

    private FakeUserJpaRepository fakeUserJpaRepository;
    private UserJpaStore userStore;

    @BeforeEach
    void setUp() {
        john = User.builder()
                .name("john")
                .email("john@email.com").build();
        sam = User.builder()
                .name("sam").build();

        johnEntity = new UserEntity(john);
        samEntity = new UserEntity(sam);

        fakeUserJpaRepository = new FakeUserJpaRepository();
        userStore = new UserJpaStore(fakeUserJpaRepository);
    }

    @Test
    void test_getAllUsers_returnsUserList() {
        fakeUserJpaRepository.save(johnEntity);
        fakeUserJpaRepository.save(samEntity);

        List<User> users = userStore.getAll();

        assertThat(users.size()).isEqualTo(2);
        List<String> usernames = users.stream().map(User::getName).collect(Collectors.toList());
        assertThat(usernames.contains("john")).isTrue();
        assertThat(usernames.contains("sam")).isTrue();
    }

    @Test
    void test_getUser_returnsUser() {
        UserEntity savedEntity = fakeUserJpaRepository.save(johnEntity);
        User user = userStore.get(savedEntity.getId());
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("john");

    }

    @Test
    void test_getUserWhenEmpty_throwsException() {
        assertThatThrownBy(() ->userStore.get("999")).isInstanceOf(NoSuchUserException.class)
                .hasMessage("No such user id : 999");
    }

    @Test
    void test_addUser_returnsUser(){
        User addUser = userStore.add(john);
        assertThat(addUser.getName()).isEqualTo("john");
        assertThat(addUser.getEmail()).isEqualTo("john@email.com");
    }

    @Test
    void test_updateUser_returnsUpdatedUser() {
        fakeUserJpaRepository.save(johnEntity);
        NameValueList nameValueList = new NameValueList();
        nameValueList.add(new NameValue("phone", "010-1234-4321"));
        john.setValues(nameValueList);

        User updatedUser = userStore.update(john);

        assertThat(updatedUser.getPhone()).isEqualTo("010-1234-4321");
        assertThat(updatedUser.getEmail()).isEqualTo("john@email.com");
    }

    @Test
    void test_deleteUser_deletesUser() {
        fakeUserJpaRepository.save(johnEntity);
        userStore.delete(john.getId());
        assertThat(userStore.getAll()).isEmpty();
    }

    @Test
    void test_getUserByEmail_returnsUserWithCorrectEmail() {
        fakeUserJpaRepository.save(johnEntity);
        User actualUser = userStore.getUserByEmail("john@email.com");
        assertThat(actualUser.getEmail()).isEqualTo("john@email.com");
    }

    @Test
    void test_getUserWithWrongEmail_throwsException() {
        assertThatThrownBy(() -> userStore.getUserByEmail("unknown@email.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("invalid email or password.");
    }
}
