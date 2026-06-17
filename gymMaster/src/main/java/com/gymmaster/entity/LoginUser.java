package com.gymmaster.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private Customer customer;
    private List<String> permissions;

    public LoginUser(Customer customer,List<String> permissions){
        this.customer=customer;
        this.permissions=permissions;
    }
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            if (authorities != null){
                return authorities;
            }
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
//        for (String permission : permissions) {
//            List<GrantedAuthority> newList = new ArrayList<>();
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
//            newList.add(authority);
//            return newList;
//        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
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
}
