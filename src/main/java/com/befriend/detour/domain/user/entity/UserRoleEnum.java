package com.befriend.detour.domain.user.entity;

public enum UserRoleEnum {

    USER(Authority.USER),  // 사용자 권한
    MANAGER(Authority.MANAGER);  // 관리자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "USER";
        public static final String MANAGER = "MANAGER";
    }

}
