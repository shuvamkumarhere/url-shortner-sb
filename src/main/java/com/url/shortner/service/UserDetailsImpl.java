package com.url.shortner.service;

import com.url.shortner.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUIS = 1L;

    private Long id;
    private String username;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String password, Long id, String username, String email, Collection<? extends GrantedAuthority> authorities) {
        this.password = password;
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    //This method converts our user object from database to spring security's userDetails object
    public static UserDetailsImpl build(User user){
        //GrantedAuthority is something which is required by spring security,
        // so it will fetch the role and convert into granted authority for spring security
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new UserDetailsImpl(
                user.getPassword(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                Collections.singletonList(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
