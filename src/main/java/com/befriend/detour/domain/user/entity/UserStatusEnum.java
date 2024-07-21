package com.befriend.detour.domain.user.entity;

public enum UserStatusEnum {

    ACTIVE(UserStatusEnum.Status.ACTIVE),  // 사용자 권한
    MANAGER(UserStatusEnum.Status.BLOCK);  // 관리자 권한

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String ACTIVE = "ACTIVE";
        public static final String BLOCK = "BLOCK";
    }

}
