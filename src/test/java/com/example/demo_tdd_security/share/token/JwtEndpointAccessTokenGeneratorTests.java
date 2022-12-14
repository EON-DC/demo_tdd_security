package com.example.demo_tdd_security.share.token;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.share.json.JsonUtil;
import com.example.demo_tdd_security.share.jwt.JwtEndpointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtEndpointAccessTokenGeneratorTests {

    private JwtEndpointAccessTokenGenerator tokenGenerator;
    private JwtSecretKey jwtSecretKet;

    private User john;

    @BeforeEach
    void setUp() {
        jwtSecretKet = new JwtSecretKey("secretKeyForTest");
        tokenGenerator = new JwtEndpointAccessTokenGenerator(jwtSecretKet);
        john = User.builder()
                .name("john")
                .email("john@email.com")
                .password("1234")
                .roles(Arrays.asList(new UserRole[]{UserRole.ROLE_USER, UserRole.ROLE_ADMIN}))
                .phone("1234-1234")
                .build();
    }

    @Test
    void tdd_for_createAccessToken_returnsString() throws Exception {
        // given
        String accessToken = tokenGenerator.createAccessToken(john.getEmail(), john.getRolesAsString());

        // when
        String subject = Jwts.parser()
                .setSigningKey(jwtSecretKet.getSecretKeyAsByte())
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();


        // then
        assertThat(subject).isEqualTo("john@email.com");
    }

    @Test
    void tdd_for_accessToken_usesIssueDate() throws Exception {
        // given
        String accessToken = tokenGenerator.createAccessToken(john.getEmail(), john.getRolesAsString());

        // when
        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKet.getSecretKeyAsByte())
                .parseClaimsJws(accessToken)
                .getBody();

        Date issuedAt = body.getIssuedAt();


        // then
        assertThat(issuedAt).isCloseTo(new Date(), 1000L);
    }

    @Test
    void tdd_for_accessToken_hasExpirationOf10Minutes() throws Exception {
        // given
        String accessToken = tokenGenerator.createAccessToken(john.getEmail(), john.getRolesAsString());

        // when
        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKet.getSecretKeyAsByte())
                .parseClaimsJws(accessToken)
                .getBody();

        Date expiration = body.getExpiration();


        // then
        assertThat(expiration).isCloseTo(new Date(new Date().getTime() + 10 * 60 * 1000L), 1000L);
    }

    @Test
    void tdd_for_accessToken_hasUserRoles() throws Exception {
        // given
        String accessToken = tokenGenerator.createAccessToken(john.getEmail(), john.getRolesAsString());

        // when
        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKet.getSecretKeyAsByte())
                .parseClaimsJws(accessToken)
                .getBody();

        List<UserRole> roles = JsonUtil.fromJsonList(JsonUtil.toJson(body.get("roles")), UserRole.class);


        // then
        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles.contains(UserRole.ROLE_USER)).isTrue();
        assertThat(roles.contains(UserRole.ROLE_ADMIN)).isTrue();
    }

    @Test
    void tdd_for_refreshToken_has30MinValidTime() throws Exception{
        // given
        String refreshToken = tokenGenerator.createRefreshToken(john.getEmail(), john.getRolesAsString());

        // when
        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKet.getSecretKeyAsByte())
                .parseClaimsJws(refreshToken)
                .getBody();
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();


        // then
        assertThat(expiration.getTime() - issuedAt.getTime()).isCloseTo(30*60*1000L, Offset.offset(1000L));
    }

}
