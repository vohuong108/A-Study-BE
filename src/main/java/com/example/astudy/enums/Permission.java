package com.example.astudy.enums;

public enum Permission {
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write"),
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
