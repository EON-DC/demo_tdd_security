package com.example.demo_tdd_security.authentication.domain;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String name;
    private String email;
    private String password;
    private String phone;

    @Builder.Default
    private List<Order> orders = new ArrayList<>();
    @Builder.Default
    private List<UserRole> roles = new ArrayList<>();

    public User() {
        id = UUID.randomUUID().toString();
        orders = new ArrayList<>();
        roles = new ArrayList<>();
    }

    public void setValues(NameValueList nameValueList) {
        for (NameValue nameValue : nameValueList.getNameValues()) {
            String name = nameValue.getName();
            String value = nameValue.getValue();
            switch (name) {
                case "name" :
                    this.name = value;
                    break;
                case "phone" :
                    this.phone = value;
                    break;
                case "password" :
                    this.password = value;
                    break;
                case "email":
                    this.email = value;
                    break;
                default:
                    throw new RuntimeException("No such field : " + name);
            }
        }
    }

    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (UserRole role: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<String> getRolesAsString() {
        return roles.stream()
                .map(UserRole::name)
                .collect(Collectors.toList());
    }
}
