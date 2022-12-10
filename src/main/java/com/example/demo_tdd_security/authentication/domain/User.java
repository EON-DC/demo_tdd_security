package com.example.demo_tdd_security.authentication.domain;

import com.example.demo_tdd_security.order.domain.Order;
import com.example.demo_tdd_security.share.domain.NameValue;
import com.example.demo_tdd_security.share.domain.NameValueList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
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
    private List<UserRole> roles = new ArrayList<>();;
    @Builder.Default
    private List<Order> orders = new ArrayList<>();


    public User() {
        id = UUID.randomUUID().toString();
        roles = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public void setValues(NameValueList nameValueList) {
        for (NameValue nameValue : nameValueList.getNameValues()) {
            String name = nameValue.getName();
            String value = nameValue.getValue();
            switch (name) {
                case "name" :
                    this.name = value;
                    break;
                case "email":
                    this.email = value;
                    break;
                case "password" :
                    this.password = value;
                    break;
                case "phone" :
                    this.phone = value;
                    break;
                default:
                    throw new IllegalArgumentException("No such fields : " + name);
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
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
                .map(userRole -> userRole.name())
                .collect(Collectors.toList());
    }
}
