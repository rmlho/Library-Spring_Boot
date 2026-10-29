package com.ramalho.library.database.model;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum EnumRoles implements GrantedAuthority {
    ADMIN,
    LIBRARY,
    READER;


    @Override
    public @Nullable String getAuthority() {
        return "ROLE_" + this.name();
    }
}
