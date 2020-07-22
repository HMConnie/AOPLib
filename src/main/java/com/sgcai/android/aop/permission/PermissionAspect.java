package com.sgcai.android.aop.permission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.sgcai.android.aop.AopSDK;
import com.sgcai.android.aop.callback.PermissionCallback;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class PermissionAspect {


    @Pointcut("execution(@com.sgcai.android.aop.permission.NeedPermission * *(..)) && @annotation(needPermission)")
    public void pointCutOnNeedPermissionMethod(NeedPermission needPermission) {

    }

    @Around("pointCutOnNeedPermissionMethod(needPermission)")
    public void handleNeedPermission(final ProceedingJoinPoint joinPoint, NeedPermission needPermission) throws Throwable {
        if (needPermission == null) {
            joinPoint.proceed();
            return;
        }

        final PermissionItem permissionItem = new PermissionItem(needPermission);
        if (!permissionItem.hasPermissions()) {
            joinPoint.proceed();
            return;
        }


        FragmentActivity target = null;
        if (joinPoint.getTarget() instanceof FragmentActivity) {
            target = (FragmentActivity) joinPoint.getTarget();
        } else if (joinPoint.getTarget() instanceof Fragment) {
            target = ((Fragment) joinPoint.getTarget()).getActivity();
        }
        if (target == null) {
            joinPoint.proceed();
            return;
        }

        AndPermission.with(target)
                .runtime()
                .permission(permissionItem.permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        for (PermissionCallback callback : AopSDK.getPermissionCallback()) {
                            callback.onPermissionDenied(permissionItem);
                        }
                    }
                })
                .start();
    }
}