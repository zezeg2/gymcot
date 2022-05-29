package com.example.security1.domain.member;

public enum Role {

    GUEST("ROLE_GUEST"),
    MEMBER("ROLE_MEMBER"),
    GYM("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


}
