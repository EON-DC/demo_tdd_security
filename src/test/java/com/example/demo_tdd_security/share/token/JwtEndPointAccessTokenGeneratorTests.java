package com.example.demo_tdd_security.share.token;

import com.example.demo_tdd_security.authentication.domain.User;
import com.example.demo_tdd_security.authentication.domain.UserRole;
import com.example.demo_tdd_security.share.json.JsonUtils;
import com.example.demo_tdd_security.share.jwt.JwtEndPointAccessTokenGenerator;
import com.example.demo_tdd_security.share.jwt.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtEndPointAccessTokenGeneratorTests {

    private JwtEndPointAccessTokenGenerator jwtTokenGenerator;

    private JwtSecretKey jwtSecretKey;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@email.com")
                .roles(Collections.singletonList(UserRole.ROLE_USER)).build();
        jwtSecretKey = new JwtSecretKey("onlyTheTestKnowsThisSecret");
        jwtTokenGenerator = new JwtEndPointAccessTokenGenerator(jwtSecretKey);
    }

    @Test
    void tdd_for_accessToken_usesUserEmail() throws Exception {
        // given
        String accessToken = jwtTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString());
        String subject = Jwts.parser()
                .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
        assertThat(subject).isEqualTo("user@email.com");
    }

    @Test
    void test_for_accessToken_usesIssueDate(){
        String accessToken = jwtTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString());

        Date issuedAt = Jwts.parser()
                .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                .parseClaimsJws(accessToken)
                .getBody()
                .getIssuedAt();

        assertThat(issuedAt).isNotNull();
    }

    @Test
    void test_for_accessToken_hasExpirationDate(){
        String accessToken = jwtTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString());

        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                .parseClaimsJws(accessToken)
                .getBody();
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();
        assertThat(expiration.getTime() - issuedAt.getTime()).isEqualTo(10 * 60 * 1000L);
    }

    @Test
    void test_for_accessToken_hasUserRoles(){
        String accessToken = jwtTokenGenerator.createAccessToken(user.getEmail(), user.getRolesAsString());

        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                .parseClaimsJws(accessToken)
                .getBody();
        List<UserRole> roles = JsonUtils.fromJsonList(JsonUtils.toJson(body.get("roles")), UserRole.class);
        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles.get(0)).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    void test_for_refreshToken_hasExpirationOf30Minutes() {
        String refreshToken = jwtTokenGenerator.createRefreshToken(user.getEmail(), user.getRolesAsString());
        Claims body = Jwts.parser()
                .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                .parseClaimsJws(refreshToken)
                .getBody();
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();
        assertThat(expiration.getTime() - issuedAt.getTime()).isEqualTo(30 * 60 * 1000L);
    }


}
