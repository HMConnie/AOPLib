package com.sgcai.android.aop.permission;


public class PermissionItem {

    public String[] permissions;
    public int requestPermissionId;


    public PermissionItem(NeedPermission permissions) {
        this.permissions = permissions.permissions();
        this.requestPermissionId = permissions.requestPermissionId();
    }

    public boolean hasPermissions() {
        return this.permissions != null && this.permissions.length > 0;
    }
}
