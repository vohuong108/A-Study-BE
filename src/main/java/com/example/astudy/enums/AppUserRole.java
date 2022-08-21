package com.example.astudy.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.astudy.enums.Permission.*;

public enum AppUserRole {
    STUDENT(Sets.newHashSet(COURSE_READ)),
    AUTHOR(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, COURSE_READ, COURSE_WRITE)),
    ADMIN_TRAINEE(Sets.newHashSet(STUDENT_READ, COURSE_READ, USER_READ)),
    SUPER_ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, COURSE_READ, COURSE_WRITE));

    private final Set<Permission> permissions;

    AppUserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Think of a GrantedAuthority as being a "permission" or a "right".
     * Roles are just "permissions" with a naming convention
     * that says that a role is a GrantedAuthority that starts with the prefix ROLE_.
     * hasAuthority('ROLE_XYZ') is the same as
     * hasRole('ROLE_XYZ') is the same as hasRole('XYZ')
     * */
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map((permission) -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
