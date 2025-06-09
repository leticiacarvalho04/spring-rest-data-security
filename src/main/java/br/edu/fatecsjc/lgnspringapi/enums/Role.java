package br.edu.fatecsjc.lgnspringapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.edu.fatecsjc.lgnspringapi.enums.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(EnumSet.of(ADMIN_CREATE, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_READ));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = permissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}