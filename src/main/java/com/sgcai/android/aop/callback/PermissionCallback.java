package com.sgcai.android.aop.callback;

import com.sgcai.android.aop.permission.PermissionItem;

public interface PermissionCallback {
    /**
     * 权限拒绝回调
     */
    void onPermissionDenied(PermissionItem permissionItem);
}
