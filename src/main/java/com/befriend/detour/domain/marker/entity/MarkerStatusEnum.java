package com.befriend.detour.domain.marker.entity;

public enum MarkerStatusEnum {

    ACTIVE(MarkerStatusEnum.Status.ACTIVE),
    DELETED(MarkerStatusEnum.Status.DELETED);

    private final String status;

    MarkerStatusEnum(String status) { this.status = status; }

    public String getStatus() { return this.status; }

    public static class Status{
        public static final String ACTIVE = "ACTIVE";
        public static final String DELETED = "DELETED";
    }


}
