package com.sgcai.android.aop.permission;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.sgcai.android.aop.AopSDK;
import com.sgcai.android.aop.callback.PermissionCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        RxPermissions rxPermissions = new RxPermissions(target);
        rxPermissions.request(permissionItem.permissions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            try {
                                joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else {
                            for (PermissionCallback callback : AopSDK.getPermissionCallback()) {
                                callback.onPermissionDenied(permissionItem);
                            }
                        }
                    }
                });


    }
}